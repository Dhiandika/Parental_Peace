const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;

const getRandomDocumentFromCollection = async (collectionRef) => {
    // Retrieve all documents from the collection
    const snapshot = await collectionRef.get();

    // Check if there are any documents in the collection
    if (snapshot.empty) {
        return null;
    }

    // Shuffle the documents randomly
    const shuffledDocs = snapshot.docs.sort(() => Math.random() - 0.5);

    // Extract the first random document
    const randomDoc = shuffledDocs[0];

    return {
        prediction_id: randomDoc.id,
        ...randomDoc.data(),
    };
};

const getRandomBellypainResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Get a reference to the "prediction_Bellypain_results" collection
            const predictionBellypainCollectionRef = db.collection("prediction_Bellypain_results");

            // Retrieve one random document from the "prediction_Bellypain_results" collection
            const randomBellypainResult = await getRandomDocumentFromCollection(predictionBellypainCollectionRef);

            if (!randomBellypainResult) {
                // Handle the case where no documents are found
                const response = h.response({
                    status: "not found",
                    error: "No documents found in the collection",
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            const response = h.response({
                status: "success",
                data: {
                    prediction_Bellypain_result: randomBellypainResult,
                },
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Log more details about the specific error
            console.error("Error retrieving random document from prediction_Bellypain_results:", error);

            const response = h.response({
                status: "bad request",
                error: "Error retrieving random document",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const getAllBellypainResults = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Retrieve all documents from the collection
            const snapshot = await predictionResultRef.get();

            // Extract data from each document
            const results = snapshot.docs.map(doc => {
                const data = doc.data();
                return {
                    source_collection: "prediction_Bellypain_results",
                    prediction_id: doc.id,
                    prediction_text: data.prediction_text,
                    additional_info: data.additional_info
                };
            });

            const response = h.response({
                status: "success",
                data: results,
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error retrieving belly pain results:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const getBellypainResultById = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract the prediction ID from the request parameters
            const { id } = request.params;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Retrieve the document with the specified prediction ID
            const doc = await predictionResultRef.doc(id).get();

            // Check if the document exists
            if (!doc.exists) {
                const response = h.response({
                    status: "not found",
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            // Extract data from the document
            const data = doc.data();

            const response = h.response({
                status: "success",
                data: {
                    prediction_id: doc.id,
                    prediction_text: data.prediction_text,
                    additional_info: data.additional_info
                },
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error retrieving belly pain result by ID:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const createBellypainResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract necessary data from the request payload
            const { prediction_text, suggestion1, suggestion2, suggestion3 } = request.payload;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Generate a unique prediction ID (for example, using the current timestamp)
            const predictionId = Date.now().toString();

            // Set the prediction_id as the document ID
            const documentRef = predictionResultRef.doc(predictionId);

            // Create a new document with the specified document ID
            await documentRef.set({
                prediction_id: predictionId,
                prediction_text: {
                    prediction_text: prediction_text,
                    additional_info: {
                        suggestion1: suggestion1,
                        suggestion2: suggestion2,
                        suggestion3: suggestion3
                    }
                }
            });

            const response = h.response({
                status: "success",
                message: "bellypain-result data has been successfully saved.",
                data: {
                    prediction_id: predictionId,
            },
            });
            response.code(201); // Respond with Created status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error creating belly pain result:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const updateBellypainResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract necessary data from the request payload
            const {
                prediction_id,
                prediction_text,
                suggestion1,
                suggestion2,
                suggestion3
            } = request.payload;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Get a reference to the specific document using the provided prediction_id
            const documentRef = predictionResultRef.doc(prediction_id);

            // Check if the document exists
            const documentSnapshot = await documentRef.get();

            if (!documentSnapshot.exists) {
                const response = h.response({
                    status: "not found",
                    message: `Bellypain result with prediction_id ${prediction_id} not found.`,
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            // Update the existing document with the new data
            await documentRef.update({
                prediction_text: {
                    prediction_text: prediction_text,
                    additional_info: {
                        suggestion1: suggestion1,
                        suggestion2: suggestion2,
                        suggestion3: suggestion3
                    }
                }
            });

            const response = h.response({
                status: "success",
                message: `Bellypain result with prediction_id ${prediction_id} has been successfully updated.`,
                data: {
                    prediction_id: prediction_id,
                },
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error updating belly pain result:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const deleteBellypainResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract the prediction ID from the request parameters
            const { id } = request.params;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Attempt to delete the document with the specified prediction ID
            await predictionResultRef.doc(id).delete();

            const response = h.response({
                status: "success",
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error deleting belly pain result:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};

const deleteAllBellypainResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Bellypain_results"
            const predictionResultRef = db.collection("prediction_Bellypain_results");

            // Retrieve all documents in the collection
            const snapshot = await predictionResultRef.get();

            // Delete each document in the collection
            const deletePromises = snapshot.docs.map(doc => doc.ref.delete());
            await Promise.all(deletePromises);

            const response = h.response({
                status: "success",
                message: "All Bellypain result documents have been successfully deleted.",
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error deleting all belly pain results:", error);
            const response = h.response({
                status: "bad request",
            });
            response.code(400);
            return response;
        }
    } else {
        // Respond with unauthorized status if the API key is incorrect
        const response = h.response({
            status: "unauthorized",
        });
        response.code(401);
        return response;
    }
};


// Export the handler functions
module.exports = {
    getRandomBellypainResult,
    createBellypainResult,
    deleteBellypainResult,
    getAllBellypainResults,
    updateBellypainResult,
    getBellypainResultById,
    deleteAllBellypainResult// Add other handler functions if needed
};