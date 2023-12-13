
const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");
const util = require('util');



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

    if (key !== api_key) {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }

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
        const filename = users_picture?.hapi?.filename;
        const data = users_picture?._data;

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

        const filePath = `./${filename}`;
        const fileExtension = filename.split('.').pop();
        const destFileName = `users/${userId}.${fileExtension}`;
        const url = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

        async function uploadFile() {
            const options = {
                destination: destFileName,
            };

            await storage.bucket(bucketName).upload(filePath, options);

            // Making file public to the internet
            await storage
                .bucket(bucketName)
                .file(destFileName)
                .makePublic();
        }

        const response = h.response(); // Initialize the response outside the try block

        await util.promisify(fs.writeFile)(filePath, data);

        try {
            await uploadFile();
            await util.promisify(fs.unlink)(filePath);

            const db = firebase_admin.firestore();
            const outputDb = db.collection("users");

            const userRecord = await firebase_admin.auth().createUser({
                email: users_email,
                password: users_password,
            });

            // Obtain the ID token from the created user
            const idToken = await firebase_admin.auth().createCustomToken(userRecord.uid);

            await outputDb.doc(userId).set({
                users_id: userId,
                users_name: users_name,
                users_email: users_email,
                users_phone: users_phone,
                users_role: users_role,
                users_picture: url,
                firebase_uid: userRecord.uid,
            });

            // Set the response data
            const responseData = {
                users_id: userRecord.uid,
                status: "success",
                token: idToken,
            };
    
            const response = h.response(responseData).code(200);
            return response; // Return the response after the successful operation
        } catch (error) {
            console.error("Error creating user:", error);
            throw error; // Rethrow the error to handle it in the catch block below
        }
    } catch (error) {
        console.error("Error in makeUsers:", error);

        const response = h.response({
            status: "bad request",
            message: "Error creating user",
        });
        response.code(500);
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

            // Get the user document to obtain the users_picture URL and firebase_uid
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
                try {
                    const filename = users_picture_url.split('/').pop(); // Extract filename from URL
                    const storage = new Storage({
                        keyFilename: path.join(__dirname, "../private/gcloud.json"),
                    });

                    // Delete the file from cloud storage
                    await storage.bucket(bucketName).file(`users/${filename}`).delete();
                } catch (storageError) {
                    console.error("Error deleting user photo from cloud storage:", storageError);
                    // Log the error and continue, since it's not critical for the overall operation
                }
            }

            // Delete user from Firebase Authentication
            const firebaseUid = userDoc.data().firebase_uid;
            await firebase_admin.auth().deleteUser(firebaseUid);

            const response = h.response({
                status: "success",
            });
            response.code(200);
            return response;
        } catch (error) {
            console.error("Error deleting user:", error);

            const response = h.response({
                status: "bad request",
                message: "Error deleting user",
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

const deleteAllUserData = async () => {
    try {
        const db = firebase_admin.firestore();
        const outputDb = db.collection("users");

        // Get all documents in the "users" collection
        const snapshot = await outputDb.get();

        // Array to store all promises for parallel execution
        const deletePromises = [];

        // Loop through each document and delete user data
        snapshot.forEach(async (doc) => {
            const userId = doc.id;

            // Delete user document from Firestore
            const deleteDocPromise = outputDb.doc(userId).delete();
            deletePromises.push(deleteDocPromise);

            // Delete user's photo from Cloud Storage
            const users_picture_url = doc.data().users_picture;
            if (users_picture_url) {
                const filename = users_picture_url.split('/').pop();
                const storage = new Storage({
                    keyFilename: path.join(__dirname, "../private/gcloud.json"),
                });

                // Delete the file from Cloud Storage
                const deleteStoragePromise = storage.bucket(bucketName).file(`users/${filename}`).delete();
                deletePromises.push(deleteStoragePromise);
            }

            // Delete user from Firebase Authentication
            const firebaseUid = doc.data().firebase_uid;
            const deleteUserPromise = firebase_admin.auth().deleteUser(firebaseUid);
            deletePromises.push(deleteUserPromise);

            console.log(`User data for ${userId} marked for deletion`);
        });

        // Wait for all promises to resolve
        await Promise.all(deletePromises);

        console.log("All user data deleted successfully");
        return "success"; // Return a value or use return Promise.resolve("success");
    } catch (error) {
        console.error("Error deleting all user data:", error);
        throw error; // Throw an error to fulfill Hapi's expectations
    }
};


module.exports = {
    getAllUsers,
    getUsers,
    makeUsers,
    editUsers,
    deleteUsers,
    deleteAllUserData,
};