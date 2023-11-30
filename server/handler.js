const firebase_admin = require("firebase-admin");
const api_key = require("./private/key.json").api_key;


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

// getAllUsers - Mengambil Semua Data Pengguna dari Firestore
const getAllUsers = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        // Mengambil semua data pengguna dari Firestore
        const snapshot = await db.collection('users').get();

        const users = [];
        snapshot.forEach((doc) => {
            users.push({
                userId: doc.id,
                ...doc.data(),
            });
        });

        const response = h.response({
            status: "success",
            data: users,
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error getting user data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to get user data.",
        });

        response.code(500); // 500 Internal Server Error
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

// addUser - Menyimpan Data Pengguna ke Firestore
const addUser = async (request, h) => {
    try {
        const db = firebase_admin.firestore();

        // Mendapatkan data pengguna dari payload
        const userData = request.payload;

        // Menyimpan data pengguna ke Firestore
        const docRef = await db.collection('users').add(userData);

        const response = h.response({
            status: "success",
            message: "User data has been successfully saved.",
            data: {
                userId: docRef.id,
            },
        });

        response.code(201); // 201 Created
        return response;
    } catch (error) {
        console.error("Error saving user data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to save user data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

// updateUser - Memperbarui Data Pengguna di Firestore
const updateUser = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        const userId = request.params.id;
        const updatedUserData = request.payload;

        // Memperbarui data pengguna di Firestore
        await db.collection('users').doc(userId).update(updatedUserData);

        const response = h.response({
            status: "success",
            message: "User data has been successfully updated.",
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error updating user data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to update user data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

// deleteUser - Menghapus Data Pengguna dari Firestore
const deleteUser = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        const userId = request.params.id;

        // Menghapus data pengguna dari Firestore
        await db.collection('users').doc(userId).delete();

        const response = h.response({
            status: "success",
            message: "User data has been successfully deleted.",
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error deleting user data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to delete user data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

// Export semua handler
module.exports = {
    getAll,
    deleteAll,
    getAllUsers,
    addUser,
    updateUser,
    deleteUser,
};