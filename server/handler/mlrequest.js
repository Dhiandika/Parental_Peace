const axios = require("axios");
const FormData = require("form-data");
const api_key = require("../private/key.json").api_key;
const ml_backend = require("../private/key.json").ml_backend;
const fs = require("fs");
const { getRandomBellypainResult } = require('./hasilbelypain');
const { getRandomBurpinResult,  } = require('./hasilburping');
const { getRandomDiscomfortResult} = require('./hasildiscomfort');
const { getRandomHungryResult } = require('./hasilhungry');
const { getRandomTiredResult } = require('./hasiltired');


async function postAudioWithAuthorization(filename) {
    const audioBuffer = fs.readFileSync(filename);

    const formData = new FormData();
    formData.append("audio", audioBuffer, { filename });

    const headers = {
        "Content-Type": `multipart/form-data; boundary=${formData._boundary}`,
        "x-api-key": `${api_key}`,
    };

    try {
        const response = await axios.post(
            `${ml_backend}/predict_audio`,
            formData,
            { headers }
        );
        console.log(response.data);
        return response.data; // Kembalikan data dari respons
    } catch (error) {
        console.error(error);
    }
}

const getRandomPredictionResult = async (predictedClass) => {
    switch (predictedClass) {
        case 'belly_pain':
            return await getRandomBellypainResult();
        case 'burping':
            return await getRandomBurpinResult();
        case 'discomfort':
            return await getRandomDiscomfortResult();
        case 'hungry':
            return await getRandomHungryResult();
        case 'tired':
            return await getRandomTiredResult();
        default:
            return null;
    }
};

const predictAudio = async (request, h) => {
    const key = request.headers["x-api-key"];
    if (key === api_key) {
        try {
            const { audio } = request.payload;

            const filename = audio.hapi.filename;
            const data = audio._data;

            let dataResponse = await new Promise((resolve, reject) => {
                fs.writeFile(filename, data, async (err) => {
                    if (err) {
                        reject(err);
                    } else {
                        try {
                            let postResponse = await postAudioWithAuthorization(filename);

                            // Add prediction result to the response
                            if (postResponse && postResponse.data && postResponse.data.predicted_class) {
                                const predictedClass = postResponse.data.predicted_class;
                                const randomResult = await getRandomPredictionResult(predictedClass);

                                // Add predicted_class and prediction_Burpin_result to the response
                                postResponse.data.predicted_class = predictedClass;
                                postResponse.data.prediction_Burpin_result = randomResult;
                            }

                            fs.unlink(filename, (err) => {
                                if (err) {
                                    console.error("Error deleting file:", err);
                                }
                            });
                            resolve(postResponse); // Return the entire response from Flask
                        } catch (error) {
                            reject(error);
                        }
                    }
                });
            });

            const response = h.response({
                status: "success",
                data: dataResponse, // Use the entire response from Flask
            });
            response.code(200);
            return response;
        } catch (error) {
            const response = h.response({
                status: "bad request",
            });
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



module.exports = { predictAudio };


