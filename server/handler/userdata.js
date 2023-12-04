
const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");


// users - Ambil Seluruh Data Users
const getAllUsers = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        const db = firebase_admin.firestore();
        const responseData = {};
        responseData["users"] = [];
        const outputDb = await db.collection("users");
        const snapshot = await outputDb.get();

        snapshot.forEach((doc) => {
            const dataObject = {};
            dataObject[doc.id] = doc.data();
            responseData["users"].push(dataObject);
        });

        const response = h.response(responseData);
        response.code(200);
        return response;
    }
    // Jika Kunci API Salah
    else {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

// users - Ambil Data Users Tertentu
const getUsers = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        // Mengambil ID Users dari Request Params
        const { id } = request.params;
        const responseData = {};

        const db = firebase_admin.firestore();
        responseData[id] = (await db.collection("users").doc(id).get()).data();

        const response = h.response(responseData);
        response.code(200);
        return response;
    }
    // Jika Kunci API Salah
    else {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

// users - Buat Data Users Baru
const makeUsers = async (request, h) => {
    const key = request.headers["x-api-key"];

    if (key === api_key) {
        try {
            const {
                users_name,
                users_email,
                users_phone,
                users_role,
                users_password,
                users_picture,
            } = request.payload;

            const userId = "u" + Date.now().toString();
            
            // Check if users_picture is defined and has 'hapi' property
            const filename = users_picture && users_picture.hapi ? users_picture.hapi.filename : null;
            const data = users_picture ? users_picture._data : null;

            if (!filename || !data) {
                const response = h.response({
                    status: "bad request",
                    message: "Invalid or missing 'users_picture' field in the payload.",
                });
                response.code(400);
                return response;
            }

            const storage = new Storage({
                keyFilename: path.join(__dirname, "../private/gcloud.json"),
            });

            // The path to your file to upload
            const filePath = `./${filename}`;
            const fileExtension = filename.split('.').pop();

            // The new ID for your GCS file
            const destFileName = `users/${userId}.${fileExtension}`;

            // file URL
            const url = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

            async function uploadFile() {
                const options = {
                    destination: destFileName,
                };

                await storage.bucket(bucketName).upload(filePath, options);

                // Making file public to the internet
                async function makePublic() {
                    await storage
                        .bucket(bucketName)
                        .file(destFileName)
                        .makePublic();
                }

                makePublic().catch(console.error);
            }

            fs.writeFile(filePath, data, async (err) => {
                if (!err) {
                    await uploadFile().catch(console.error);
                    fs.unlink(filePath, (err) => {
                        if (err) {
                            console.error("Error deleting file:", err);
                        }
                    });
                }
            });

            const db = firebase_admin.firestore();
            const outputDb = db.collection("users");

            const userRecord = await firebase_admin.auth().createUser({
                email: users_email,
                password: users_password,
            });

            const uid = userRecord.uid;

            await outputDb.doc(userId).set({
                users_id: userId,
                users_name: users_name,
                users_email: users_email,
                users_phone: users_phone,
                users_role: users_role,
                users_picture: url,
                firebase_uid: uid,
            });

            const response = h.response({
                status: "success",
            });
            response.code(200);
            return response;
        } catch (error) {
            console.error("Error creating user:", error);

            const response = h.response({
                status: "bad request",
            });

            if (error.code === "auth/email-already-exists") {
                response.message = "Email address is already in use";
                response.code(400);
            } else {
                response.code(500);
            }

            return response;
        }
    } else {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};


// editUsers - Edit Data Users
const editUsers = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    
    // Jika Kunci API Benar
    if (key === api_key) {
        try {
            const { userId, users_name, users_email, users_phone, users_role, users_picture } = request.payload;

            // Log userId to debug
            console.log('User ID:', userId);

            // Check if userId is undefined or an empty string
            if (!userId || typeof userId !== 'string') {
                const response = h.response({
                    status: 'bad request',
                    message: 'Invalid or missing userId in the payload.',
                });
                response.code(400);
                return response;
            }

            const db = firebase_admin.firestore();
            const userRef = db.collection("users").doc(userId);

            // Check if the user exists
            const userSnapshot = await userRef.get();
            if (!userSnapshot.exists) {
                const response = h.response({
                    status: "not found",
                    message: "User not found",
                });
                response.code(404); // Not Found
                return response;
            }

            // Update user data in Firestore
            await userRef.update({
                users_name: users_name,
                users_phone: users_phone,
                users_role: users_role,
            });

            // If there's a new profile picture, update it in Google Cloud Storage
            if (users_picture) {
                const filename = users_picture.hapi.filename;
                const data = users_picture._data;

                const storage = new Storage({
                    keyFilename: path.join(__dirname, "../private/gcloud.json"),
                });

                const filePath = `./${filename}`;
                const fileExtension = filename.split('.').pop();
                const destFileName = `users/${userId}.${fileExtension}`;
                const url = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

                // Upload the new profile picture
                async function uploadFile() {
                    const options = {
                        destination: destFileName,
                    };

                    await storage.bucket(bucketName).upload(filePath, options);

                    // Making file public to the internet
                    async function makePublic() {
                        await storage
                            .bucket(bucketName)
                            .file(destFileName)
                            .makePublic();
                    }

                    makePublic().catch(console.error);
                }

                fs.writeFile(filename, data, async (err) => {
                    if (!err) {
                        await uploadFile().catch(console.error);
                        fs.unlink(filename, (err) => {
                            if (err) {
                                console.error("Error deleting file:", err);
                            }
                        });
                    }
                });

                // Update the profile picture URL in Firestore
                await userRef.update({
                    users_picture: url,
                });
            }

            const response = h.response({
                status: "success",
            });
            response.code(200);
            return response;
        } catch (error) {
            console.error("Error editing user:", error);

            const response = h.response({
                status: "bad request",
            });

            // Check if the error is due to an existing email
            if (error.code === "auth/email-already-exists") {
                response.message = "Email address is already in use";
                response.code(400); // Bad Request
            } else {
                response.code(500); // Internal Server Error
            }

            return response;
        }
    } else {
        // Jika Kunci API Salah
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};


// users - Hapus Data Users Tertentu
const deleteUsers = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        const { id } = request.params;

        try {
            const db = firebase_admin.firestore();
            const outputDb = db.collection("users");

            // Get the user document to obtain the users_picture URL
            const userDoc = await outputDb.doc(id).get();

            // Check if the user exists
            if (!userDoc.exists) {
                const response = h.response({
                    status: "not found",
                    message: "User not found",
                });
                response.code(404); // Not Found
                return response;
            }

            // Delete user document from Firestore
            await outputDb.doc(id).delete();

            // Delete user's photo from cloud storage
            const users_picture_url = userDoc.data().users_picture;
            if (users_picture_url) {
                const filename = users_picture_url.split('/').pop(); // Extract filename from URL
                const storage = new Storage({
                    keyFilename: path.join(__dirname, "../private/gcloud.json"),
                });

                // Delete the file from cloud storage
                await storage.bucket(bucketName).file(`users/${filename}`).delete();
            }

            const response = h.response({
                status: "success",
            });
            response.code(200);
            return response;
        } catch (error) {
            console.error("Error deleting user:", error);

            const response = h.response({
                status: "bad request",
            });
            response.code(500); // Internal Server Error
            return response;
        }
    }
    // Jika Kunci API Salah
    else {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

module.exports = {
    getAllUsers,
    getUsers,
    makeUsers,
    editUsers,
    deleteUsers
};