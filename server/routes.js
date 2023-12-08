// Import dari Handler
const { getAll, deleteAll } = require("./handler/alldata");

const {
    getAllArticles,
    getArticle,
    addArticle,
    updateArticle,
    deleteArticle,
} = require("./handler/artikeldata");

const {
    getAllUsers,
    getUsers,
    makeUsers,
    editUsers,
    deleteUsers,
} = require("./handler/userdata");

const { getDocumentsFromSpecificCollections } = require("./handler/predict");

const {
    getRandomBellypainResult,
    createBellypainResult,
    updateBellypainResult,
    deleteBellypainResult,
    getAllBellypainResults,
    getBellypainResultById,
    deleteAllBellypainResult,
} = require("./handler/hasilbelypain");

const {
    getRandomBurpinResult,
    getAllBurpingResults,
    getBurpingResultById,
    createBurpingResult,
    updateBurpingResult,
    deleteByIdBurpingResult,
    deleteAllBurpingResults,
} = require("./handler/hasilburping");

const {
    getRandomDiscomfortResult,
    getAllDiscomfortResults,
    getDiscomfortResultById,
    createDiscomfortResult,
    updateDiscomfortResult,
    deleteDiscomfortResultById,
    deleteAllDiscomfortResults,
} = require("./handler/hasildiscomfort");

const {
    getRandomHungryResult,
    getAllHungryResults,
    getHungryResultById,
    createHungryResult,
    updateHungryResult,
    deleteHungryResultById,
    deleteAllHungryResults,
} = require("./handler/hasilhungry");

const {
    getRandomTiredResult,
    getAllTiredResults,
    getTiredResultById,
    createTiredResult,
    updateTiredResult,
    deleteTiredResultById,
    deleteAllTiredResults,
} = require("./handler/hasiltired");

const routes = [
    // all - Ambil Seluruh Data Database
    {
        method: "GET",
        path: "/",
        handler: getAll,
    },
    // all - Hapus Seluruh Data Database
    {
        method: "DELETE",
        path: "/",
        handler: deleteAll,
    },
    // users - Ambil Seluruh Data Users
    {
        method: "GET",
        path: "/users",
        handler: getAllUsers,
    },

    // users - Ambil Data Users Tertentu
    {
        method: "GET",
        path: "/users/{id}",
        handler: getUsers,
    },

    // users - Buat Data Users Baru
    {
        method: "POST",
        path: "/users",
        handler: makeUsers,
        options: {
            payload: {
                maxBytes: 10485760,
                multipart: true,
                output: "stream",
            },
        },
    },

    // users - Edit Data Users Tertentu
    {
        method: "PUT",
        path: "/users/{id}",
        handler: editUsers,
        options: {
            payload: {
                maxBytes: 10485760,
                multipart: true,
                output: "stream",
            },
        },
    },

    // users - Hapus Data Users Tertentu
    {
        method: "DELETE",
        path: "/users/{id}",
        handler: deleteUsers,
    },
    // getAllArticles - Mengambil Semua Data Artikel dari Firestore
    {
        method: "GET",
        path: "/articles",
        handler: getAllArticles,
    },

    // getArticle - Ambil Data Artikel Tertentu
    {
        method: "GET",
        path: "/articles/{id}",
        handler: getArticle,
    },

    // addArticle - Menyimpan Data Artikel ke Firestore
    {
        method: "POST",
        path: "/articles",
        handler: addArticle,
        options: {
            payload: {
                maxBytes: 10485760,
                multipart: true,
                output: "stream",
            },
        },
    },

    // updateArticle - Memperbarui Data Artikel di Firestore
    {
        method: "PUT",
        path: "/articles/{id}",
        handler: updateArticle,
        options: {
            payload: {
                maxBytes: 10485760,
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "GET",
        path: "/getDocuments",
        handler: getDocumentsFromSpecificCollections,
    },

    // deleteArticle - Menghapus Data Artikel dari Firestore
    {
        method: "DELETE",
        path: "/articles/{id}",
        handler: deleteArticle,
    },
    // bellypain-result
    {
        method: "GET",
        path: "/bellypain-result",
        handler: getAllBellypainResults,
    },
    {
        method: "GET",
        path: "/bellypain-result/{id}",
        handler: getBellypainResultById,
    },
    {
        method: "POST",
        path: "/bellypain-result",
        handler: createBellypainResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "PUT",
        path: "/bellypain-result/{id}",
        handler: updateBellypainResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "DELETE",
        path: "/bellypain-result/{id}",
        handler: deleteBellypainResult,
    },
    {
        method: "DELETE",
        path: "/bellypain-result",
        handler: deleteAllBellypainResult,
    },
    // burping-result
    {
        method: "GET",
        path: "/burping-result",
        handler: getAllBurpingResults,
    },
    {
        method: "GET",
        path: "/burping-result/{id}",
        handler: getBurpingResultById,
    },
    {
        method: "POST",
        path: "/burping-result",
        handler: createBurpingResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "PUT",
        path: "/burping-result/{id}",
        handler: updateBurpingResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "DELETE",
        path: "/burping-result/{id}",
        handler: deleteByIdBurpingResult,
    },
    {
        method: "DELETE",
        path: "/burping-result",
        handler: deleteAllBurpingResults,
    },
    // DiscomfortResults,
    {
        method: "GET",
        path: "/discomfort-result",
        handler: getAllDiscomfortResults,
    },
    {
        method: "GET",
        path: "/discomfort-result/{id}",
        handler: getDiscomfortResultById,
    },
    {
        method: "POST",
        path: "/discomfort-result",
        handler: createDiscomfortResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "PUT",
        path: "/discomfort-result/{id}",
        handler: updateDiscomfortResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "DELETE",
        path: "/discomfort-result/{id}",
        handler: deleteDiscomfortResultById,
    },
    {
        method: "DELETE",
        path: "/discomfort-result",
        handler: deleteAllDiscomfortResults,
    },
    // HungryResults,
    {
        method: "GET",
        path: "/hungry-result",
        handler: getAllHungryResults,
    },
    {
        method: "GET",
        path: "/hungry-result/{id}",
        handler: getHungryResultById,
    },
    {
        method: "POST",
        path: "/hungry-result",
        handler: createHungryResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "PUT",
        path: "/hungry-result/{id}",
        handler: updateHungryResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "DELETE",
        path: "/hungry-result/{id}",
        handler: deleteHungryResultById,
    },
    {
        method: "DELETE",
        path: "/hungry-result",
        handler: deleteAllHungryResults,
    },
    // TiredResults,
    {
        method: "GET",
        path: "/tired-result",
        handler: getAllTiredResults,
    },
    {
        method: "GET",
        path: "/tired-result/{id}",
        handler: getTiredResultById,
    },
    {
        method: "POST",
        path: "/tired-result",
        handler: createTiredResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "PUT",
        path: "/tired-result/{id}",
        handler: updateTiredResult,
        options: {
            payload: {
                maxBytes: 10485760, // Adjust as needed
                multipart: true,
                output: "stream",
            },
        },
    },
    {
        method: "DELETE",
        path: "/tired-result/{id}",
        handler: deleteTiredResultById,
    },
    {
        method: "DELETE",
        path: "/tired-result",
        handler: deleteAllTiredResults,
    },
    // Add a new route for getting random  results
    {
        method: "GET",
        path: "/random_bellypain_results",
        handler: getRandomBellypainResult,
    },
    // Add a new route for getting random  results
    {
        method: "GET",
        path: "/random_burping_results",
        handler: getRandomBurpinResult,
    },
       // Add a new route for getting random  results
    {
        method: "GET",
        path: "/random_discomfort_results",
        handler: getRandomDiscomfortResult,
    }, 
           // Add a new route for getting random  results
    {
        method: "GET",
        path: "/random_hungry_results",
        handler: getRandomHungryResult,
    },
    // Add a new route for getting random  results
    {
        method: "GET",
        path: "/random_tired_results",
        handler: getRandomTiredResult,
    },

];

// Export Routes
module.exports = routes;
