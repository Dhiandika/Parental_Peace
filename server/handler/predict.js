const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;

const getDocumentsFromSpecificCollections = async (request, h) => {
    // Extract the API key from the request header
    const key = request.headers["x-api-key"];

    // Check if the API key is correct
    if (key === api_key) {
        try {
            // Get a reference to the Firestore database
            const db = firebase_admin.firestore();

            // Get references to the specified collections
            const predictionBellypainCollectionRef = db.collection("prediction_Bellypain_results");
            const predictionBurpinCollectionRef = db.collection("prediction_Burpin_results");
            const predictionDiscomfortCollectionRef = db.collection("prediction_Discomfort_results");
            const predictionHungryCollectionRef = db.collection("prediction_Hungry_results");
            const predictionTiredCollectionRef = db.collection("prediction_Tired_results");

            // Retrieve all documents from the specified collections
            const predictionBellypainSnapshot = await predictionBellypainCollectionRef.get();
            const predictionBurpinSnapshot = await predictionBurpinCollectionRef.get();
            const predictionDiscomfortSnapshot = await predictionDiscomfortCollectionRef.get();
            const predictionHungrySnapshot = await predictionHungryCollectionRef.get();
            const predictionTiredSnapshot = await predictionTiredCollectionRef.get();

            // Extract data from each document
            const predictionBellypainResults = predictionBellypainSnapshot.docs.map(doc => ({
                source_collection: "prediction_Bellypain_results",
                prediction_id: doc.id,
                prediction_text: doc.data().prediction_text,
                additional_info: doc.data().additional_info
            }));

            const predictionBurpinResults = predictionBurpinSnapshot.docs.map(doc => ({
                source_collection: "prediction_Burpin_results",
                prediction_id: doc.id,
                prediction_text: doc.data().prediction_text,
                additional_info: doc.data().additional_info
            }));

            const predictionDiscomfortResults = predictionDiscomfortSnapshot.docs.map(doc => ({
                source_collection: "prediction_Discomfort_results",
                prediction_id: doc.id,
                prediction_text: doc.data().prediction_text,
                additional_info: doc.data().additional_info
            }));

            const predictionHungryResults = predictionHungrySnapshot.docs.map(doc => ({
                source_collection: "prediction_Hungry_results",
                prediction_id: doc.id,
                prediction_text: doc.data().prediction_text,
                additional_info: doc.data().additional_info
            }));

            const predictionTiredResults = predictionTiredSnapshot.docs.map(doc => ({
                source_collection: "prediction_Tired_results",
                prediction_id: doc.id,
                prediction_text: doc.data().prediction_text,
                additional_info: doc.data().additional_info
            }));

            const response = h.response({
                status: "success",
                data: {
                    prediction_Bellypain_results: predictionBellypainResults,
                    prediction_Burpin_results: predictionBurpinResults,
                    prediction_Discomfort_results: predictionDiscomfortResults,
                    prediction_Hungry_results: predictionHungryResults,
                    prediction_Tired_results: predictionTiredResults,
                },
            });
            response.code(200); // Respond with OK status
            return response;
        } catch (error) {
            // Log more details about the specific error
            console.error("Error retrieving documents from the specific collections:", error);

            const response = h.response({
                status: "bad request",
                error: "Error retrieving documents",
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

module.exports = {
    getDocumentsFromSpecificCollections,
};