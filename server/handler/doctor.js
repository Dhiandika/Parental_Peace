const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");

// Function to get all doctors
const getAllDoctors = async (request, h) => {
    try {
        const key = request.headers['x-api-key'];

        // Check if the API key is correct
        if (key === api_key) {
            // Get a reference to the 'doctors' collection in Firebase
            const doctorsCollection = firebase_admin.firestore().collection('doctors');

            // Get all doctors from the collection
            const doctorsSnapshot = await doctorsCollection.get();

            // Extract doctor data from the snapshot
            const doctors = [];
            doctorsSnapshot.forEach((doctorDoc) => {
                const doctorData = doctorDoc.data();
                doctorData.id = doctorDoc.id;
                doctors.push(doctorData);
            });

            const response = h.response({
                status: 'success',
                data: doctors,
            });
            response.code(200); // OK
            return response;
        } else {
            const response = h.response({
                status: 'unauthorized',
            });
            response.code(401);
            return response;
        }
    } catch (error) {
        console.error('Error getting all doctors:', error);

        const response = h.response({
            status: 'error',
            message: 'Error getting all doctors',
        });
        response.code(500); // Internal Server Error
        return response;
    }
};

// Function to get a specific doctor by ID
const getDoctorById = async (request, h) => {
    try {
        const key = request.headers['x-api-key'];
        const { id } = request.params;

        // Check if the API key is correct
        if (key === api_key) {
            // Get a reference to the 'doctors' collection in Firebase
            const doctorsCollection = firebase_admin.firestore().collection('doctors');

            // Get the doctor document by ID
            const doctorDoc = await doctorsCollection.doc(id).get();

            // Check if the doctor exists
            if (doctorDoc.exists) {
                const doctorData = doctorDoc.data();
                doctorData.id = doctorDoc.id;

                const response = h.response({
                    status: 'success',
                    data: doctorData,
                });
                response.code(200); // OK
                return response;
            } else {
                const response = h.response({
                    status: 'not found',
                    message: 'Doctor not found',
                });
                response.code(404); // Not Found
                return response;
            }
        } else {
            const response = h.response({
                status: 'unauthorized',
            });
            response.code(401);
            return response;
        }
    } catch (error) {
        console.error('Error getting doctor by ID:', error);

        const response = h.response({
            status: 'error',
            message: 'Error getting doctor by ID',
        });
        response.code(500); // Internal Server Error
        return response;
    }
};

// Function to create a new doctor with image
const makeDoctor = async (request, h) => {
    // Get the API key from the request headers
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key !== api_key) {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }

    try {
        // Extract doctor data and image from the request payload
        const { name, specialis, rate, status, Harga } = request.payload;
        const { image } = request.payload; // Assuming image is part of the payload

        // Create a new doctor document in Firestore
        const db = firebase_admin.firestore();
        const outputDb = db.collection("doctors");

        // Generate a unique ID for the new doctor
        const doctorId = "d" + Date.now().toString();

        // Set the data for the new doctor
        await outputDb.doc(doctorId).set({
            doctorId,
            name,
            specialis,
            rate,
            status,
            Harga,
        });

        // Handle the doctor's image upload to Cloud Storage
        if (image) {
            const filename = image.hapi.filename;
            const data = image._data;

            const storage = new Storage({
                keyFilename: path.join(__dirname, "../private/gcloud.json"),
            });

            const filePath = `./${filename}`;
            const destFileName = `doctors/${doctorId}/${filename}`;
            const imageUrl = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

            // Upload the image to Cloud Storage
            await storage.bucket(bucketName).file(destFileName).save(data);

            // Making file public to the internet
            await storage.bucket(bucketName).file(destFileName).makePublic();

            // Update the doctor document with the image URL
            await outputDb.doc(doctorId).update({
                imageUrl,
            });
        }

        const response = h.response({
            status: "success",
            message: "Doctor created successfully",
            doctorId,
        });
        response.code(200);
        return response;
    } catch (error) {
        console.error("Error creating doctor:", error);

        const response = h.response({
            status: "bad request",
            message: "Error creating doctor",
        });
        response.code(500);
        return response;
    }
};

// Function to edit an existing doctor
const editDoctor = async (request, h) => {
    // Get the API key from the request headers
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key !== api_key) {
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }

    try {
        // Extract doctor data and image from the request payload
        const { doctorId, name, specialis, rate, status, Harga, image } = request.payload;

        // Check if doctorId is undefined or an empty string
        if (!doctorId || typeof doctorId !== 'string') {
            const response = h.response({
                status: 'bad request',
                message: 'Invalid or missing doctorId in the payload.',
            });
            response.code(400);
            return response;
        }

        // Create a reference to the doctor document in Firestore
        const db = firebase_admin.firestore();
        const doctorRef = db.collection("doctors").doc(doctorId);

        // Check if the doctor exists
        const doctorSnapshot = await doctorRef.get();
        if (!doctorSnapshot.exists) {
            const response = h.response({
                status: "not found",
                message: "Doctor not found",
            });
            response.code(404); // Not Found
            return response;
        }

        // Update doctor data in Firestore
        await doctorRef.update({
            name,
            specialis,
            rate,
            status,
            Harga,
        });

        // Handle the doctor's image update in Cloud Storage
        if (image) {
            const filename = image.hapi.filename;
            const data = image._data;

            const storage = new Storage({
                keyFilename: path.join(__dirname, "../private/gcloud.json"),
            });

            const filePath = `./${filename}`;
            const destFileName = `doctors/${doctorId}/${filename}`;
            const imageUrl = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

            // Upload the updated image to Cloud Storage
            await storage.bucket(bucketName).file(destFileName).save(data);

            // Making file public to the internet
            await storage.bucket(bucketName).file(destFileName).makePublic();

            // Update the doctor document with the updated image URL
            await doctorRef.update({
                imageUrl,
            });
        }

        const response = h.response({
            status: "success",
            message: "Doctor updated successfully",
            doctorId,
        });
        response.code(200);
        return response;
    } catch (error) {
        console.error("Error editing doctor:", error);

        const response = h.response({
            status: "bad request",
            message: "Error editing doctor",
        });
        response.code(500);
        return response;
    }
};


// Function to delete all doctors
const deleteAllDoctors = async (request, h) => {
    try {
        const key = request.headers['x-api-key'];

        // Check if the API key is correct
        if (key === api_key) {
            // Get a reference to the doctors collection in Firebase
            const doctorsRef = firebase_admin.firestore().collection('doctors');

            // Get all doctors documents
            const snapshot = await doctorsRef.get();

            // Delete each doctor document
            const deletePromises = snapshot.docs.map(async (doc) => {
                await doc.ref.delete();
            });

            // Wait for all delete operations to complete
            await Promise.all(deletePromises);

            const response = h.response({
                status: 'success',
                message: 'All doctors deleted successfully',
            });
            response.code(200); // OK
            return response;
        } else {
            const response = h.response({
                status: 'unauthorized',
            });
            response.code(401);
            return response;
        }
    } catch (error) {
        console.error('Error deleting all doctors:', error);

        const response = h.response({
            status: 'error',
            message: 'Error deleting all doctors',
        });
        response.code(500); // Internal Server Error
        return response;
    }
};

// Function to delete a doctor by ID
const deleteDoctorById = async (request, h) => {
    try {
        const key = request.headers['x-api-key'];

        // Check if the API key is correct
        if (key === api_key) {
            const { id } = request.params;

            // Get a reference to the doctor document in Firebase
            const doctorRef = firebase_admin.firestore().collection('doctors').doc(id);

            // Check if the doctor exists
            const doctorSnapshot = await doctorRef.get();
            if (!doctorSnapshot.exists) {
                const response = h.response({
                    status: 'not found',
                    message: 'Doctor not found',
                });
                response.code(404); // Not Found
                return response;
            }

            // Delete the doctor document
            await doctorRef.delete();

            const response = h.response({
                status: 'success',
                message: 'Doctor deleted successfully',
            });
            response.code(200); // OK
            return response;
        } else {
            const response = h.response({
                status: 'unauthorized',
            });
            response.code(401);
            return response;
        }
    } catch (error) {
        console.error('Error deleting doctor by ID:', error);

        const response = h.response({
            status: 'error',
            message: 'Error deleting doctor by ID',
        });
        response.code(500); // Internal Server Error
        return response;
    }
};

module.exports = {
    getAllDoctors,
    getDoctorById,
    makeDoctor,
    editDoctor,
    deleteAllDoctors,
    deleteDoctorById,
    // Add other functions as needed
};