const axios = require("axios");
const FormData = require("form-data");
const api_key = require("../private/key.json").api_key;
const ml_backend = require("../private/key.json").ml_backend;
const fs = require("fs");
const firebase_admin = require("firebase-admin");
const ffmpeg = require("fluent-ffmpeg"); // Added dependency

// Function to convert audio to WAV format
const convertToWAV = async (inputFilePath, outputFilePath) => {
    return new Promise((resolve, reject) => {
        const tempOutputFilePath = outputFilePath.replace(/\.\w+$/, '_temp.wav');

        ffmpeg()
            .input(inputFilePath)
            .audioCodec('pcm_s16le')
            .audioFrequency(44100)
            .toFormat('wav')
            .on('end', () => {
                console.log('Conversion completed');

                // Move or replace the temporary file with the original file
                fs.renameSync(tempOutputFilePath, outputFilePath);

                resolve(outputFilePath);
            })
            .on('error', (err) => {
                console.error('Error during conversion:', err);
                reject(err);
            })
            .save(tempOutputFilePath);
    });
};


const postAudioWithAuthorization = async (filename) => {
    try {
        const audioBuffer = fs.readFileSync(filename);

        const formData = new FormData();
        formData.append("audio", audioBuffer, { filename });

        const headers = {
            "Content-Type": `multipart/form-data; boundary=${formData._boundary}`,
            "x-api-key": `${api_key}`,
        };

        const response = await axios.post(`${ml_backend}/predict_audio`, formData, { headers });
        return response.data;
    } catch (error) {
        console.error("Error posting audio:", error);
        throw error; // Re-throw the error for the caller to handle
    }
};

const cleanupFile = (filename) => {
    fs.unlink(filename, (err) => {
        if (err) {
            console.error("Error deleting file:", err);
        }
    });
};

const predictAudiokon = async (request, h) => {
    const key = request.headers["x-api-key"];
    if (key !== api_key) {
        const unauthorizedResponse = h.response({ status: "unauthorized" });
        unauthorizedResponse.code(401);
        return unauthorizedResponse;
    }

    try {
        const db = firebase_admin.firestore(); // Initialize Firestore database

        const { audio } = request.payload;
        const filename = audio.hapi.filename;
        const data = audio._data;

        // Save the audio file in the format expected by the machine learning backend
        await fs.promises.writeFile(filename, data);

        // Convert audio to WAV format
        const wavFilename = filename.replace(/\.\w+$/, '.wav');
        await convertToWAV(filename, wavFilename);

        try {
            // Send the converted audio file to the machine learning backend
            const dataResponse = await postAudioWithAuthorization(wavFilename);

            // Clean up the files after usage
            cleanupFile(filename);
            cleanupFile(wavFilename);

            const predictedClass = dataResponse.predicted_class;

            let randomResult;

            switch (predictedClass) {
                case "burping":
                    // Get a reference to the "prediction_Burpin_results" collection
                    const predictionBurpinCollectionRef = db.collection(
                        "prediction_Burpin_results"
                    );
                    // Retrieve one random document from the "prediction_Burpin_results" collection
                    randomResult = await getRandomDocumentFromCollection(
                        predictionBurpinCollectionRef
                    );
                    break;
                case "belly_pain":
                    // Get a reference to the "prediction_Bellypain_results" collection
                    const predictionBellypainCollectionRef = db.collection("prediction_Bellypain_results");
                    // Retrieve one random document from the "prediction_Bellypain_results" collection
                    randomResult = await getRandomDocumentFromCollection(predictionBellypainCollectionRef);
                    break;
                case "discomfort":
                    // Get a reference to the "prediction_Discomfort_results" collection
                    const predictionDiscomfortCollectionRef = db.collection("prediction_Discomfort_results");
                    randomResult = await getRandomDocumentFromCollection(predictionDiscomfortCollectionRef);
                    break;
                case "hungry":
                    // Get a reference to the "prediction_Hungry_results" collection
                    const predictionHungryCollectionRef = db.collection("prediction_Hungry_results");
                    // Retrieve one random document from the "prediction_Hungry_results" collection
                    randomResult = await getRandomDocumentFromCollection(predictionHungryCollectionRef);
                    break;
                case "tired":
                    // Get a reference to the "prediction_Tired_results" collection
                    const predictionTiredCollectionRef = db.collection("prediction_Tired_results");
                    // Retrieve one random document from the "prediction_Tired_results" collection
                    randomResult = await getRandomDocumentFromCollection(predictionTiredCollectionRef);
                    break;
                default:
                    throw new Error("Unknown predicted class: " + predictedClass);
            }

            // Return success response
            const successResponse = h.response({
                status: "success",
                data: {
                    dataResponse,
                    randomResult,
                },
            });
            successResponse.code(200);
            return successResponse;
        } catch (error) {
            // Clean up files after an error
            cleanupFile(filename);
            cleanupFile(wavFilename);
            throw error; // Re-throw the error for the caller to handle
        }
    } catch (error) {
        console.error("Error processing audio:", error);
        const badRequestResponse = h.response({ status: "bad request" });
        return badRequestResponse;
    }
};



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


module.exports = { predictAudiokon };