const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;

// Helper function to retrieve one random document from a collection
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

const getRandomDiscomfortResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Get a reference to the "prediction_Discomfort_results" collection
            const predictionDiscomfortCollectionRef = db.collection("prediction_Discomfort_results");

            // Retrieve one random document from the "prediction_Discomfort_results" collection
            const randomDiscomfortResult = await getRandomDocumentFromCollection(predictionDiscomfortCollectionRef);

            if (!randomDiscomfortResult) {
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
                    prediction_Discomfort_result: randomDiscomfortResult,
                },
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Log more details about the specific error
            console.error("Error retrieving random document from prediction_Discomfort_results:", error);

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


const getAllDiscomfortResults = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

            // Retrieve all documents from the collection
            const snapshot = await predictionResultRef.get();

            // Extract data from each document
            const results = snapshot.docs.map(doc => {
                const data = doc.data();
                return {
                    source_collection: "prediction_Discomfort_results",
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
            console.error("Error retrieving Discomfort results:", error);
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

const getDiscomfortResultById = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract the document ID from the request parameters
            const { id } = request.params;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

            // Get a reference to the specific document using the provided ID
            const documentRef = predictionResultRef.doc(id);

            // Retrieve the document
            const documentSnapshot = await documentRef.get();

            // Check if the document exists
            if (!documentSnapshot.exists) {
                const response = h.response({
                    status: "not found",
                    message: `Discomfort result with prediction_id ${id} not found.`,
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            // Extract data from the document
            const data = documentSnapshot.data();

            const result = {
                source_collection: "prediction_Discomfort_results",
                prediction_id: documentSnapshot.id,
                prediction_text: data.prediction_text,
                additional_info: data.additional_info
            };

            const response = h.response({
                status: "success",
                data: result,
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error retrieving Discomfort result by ID:", error);
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

const createDiscomfortResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract necessary data from the request payload
            const { prediction_text, suggestion1, suggestion2, suggestion3 } = request.payload;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

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
                message: "Discomfort result data has been successfully saved.",
                data: {
                    prediction_id: predictionId,
                },
            });
            response.code(201); // Respond with Created status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error creating Discomfort result:", error);
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

const updateDiscomfortResult = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract the document ID from the request parameters
            const { id } = request.params;

            // Extract necessary data from the request payload
            const { prediction_text, suggestion1, suggestion2, suggestion3 } = request.payload;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

            // Get a reference to the specific document using the provided ID
            const documentRef = predictionResultRef.doc(id);

            // Check if the document exists
            const documentSnapshot = await documentRef.get();

            if (!documentSnapshot.exists) {
                const response = h.response({
                    status: "not found",
                    message: `Discomfort result with prediction_id ${id} not found.`,
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            // Update the document with the specified data
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
                message: `Discomfort result with prediction_id ${id} has been successfully updated.`,
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error updating Discomfort result:", error);
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

const deleteDiscomfortResultById = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Extract the document ID from the request parameters
            const { id } = request.params;

            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

            // Get a reference to the specific document using the provided ID
            const documentRef = predictionResultRef.doc(id);

            // Check if the document exists
            const documentSnapshot = await documentRef.get();

            if (!documentSnapshot.exists) {
                const response = h.response({
                    status: "not found",
                    message: `Discomfort result with prediction_id ${id} not found.`,
                });
                response.code(404); // Respond with Not Found status
                return response;
            }

            // Delete the document
            await documentRef.delete();

            const response = h.response({
                status: "success",
                message: `Discomfort result with prediction_id ${id} has been successfully deleted.`,
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error deleting Discomfort result:", error);
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

const deleteAllDiscomfortResults = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Assuming you have a collection named "prediction_Discomfort_results"
            const predictionResultRef = db.collection("prediction_Discomfort_results");

            // Retrieve all documents from the collection
            const snapshot = await predictionResultRef.get();

            // Check if there are any documents to delete
            if (snapshot.empty) {
                const response = h.response({
                    status: "success",
                    message: "No Discomfort results to delete.",
                });
                response.code(200); // Respond with OK status
                return response;
            }

            // Delete all documents in the collection
            const batch = db.batch();
            snapshot.docs.forEach(doc => {
                batch.delete(doc.ref);
            });
            await batch.commit();

            const response = h.response({
                status: "success",
                message: "All Discomfort results have been successfully deleted.",
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Handle errors and respond with a bad request status
            console.error("Error deleting all Discomfort results:", error);
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
    getRandomDiscomfortResult,
    getAllDiscomfortResults,
    getDiscomfortResultById,
    createDiscomfortResult,
    updateDiscomfortResult,
    deleteDiscomfortResultById,
    deleteAllDiscomfortResults,// Add other handler functions if needed
};


