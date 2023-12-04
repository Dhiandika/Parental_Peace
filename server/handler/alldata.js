const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;


// all - Ambil Seluruh Data Database
const getAll = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        const db = firebase_admin.firestore();
        const allCollection = [];
        const responseData = {};

        // Melist seluruh collection firestore
        await db.listCollections().then((collections) => {
            for (let collection of collections) {
                allCollection.push(`${collection.id}`);
            }
        });

        // Mengambil seluruh document di collection
        for (let collection of allCollection) {
            responseData[collection] = [];
            const outputDb = await db.collection(`${collection}`);
            const snapshot = await outputDb.get();
            snapshot.forEach((doc) => {
                const dataObject = {};
                dataObject[doc.id] = doc.data();
                responseData[collection].push(dataObject);
            });
        }

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

// all - Hapus Seluruh Data Database
const deleteAll = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        const db = firebase_admin.firestore();
        const allCollection = [];

        // Melist seluruh collection firestore
        await db.listCollections().then((collections) => {
            for (let collection of collections) {
                allCollection.push(`${collection.id}`);
            }
        });

        // Mengambil seluruh document di collection
        for (let collection of allCollection) {
            const outputDb = await db.collection(`${collection}`);
            const snapshot = await outputDb.get();
            snapshot.forEach(async (doc) => {
                await db.collection(collection).doc(doc.id).delete();
            });
        }

        const response = h.response({
            status: "success",
        });
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

module.exports = {
    getAll,
    deleteAll,
};