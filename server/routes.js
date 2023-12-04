// Import dari Handler

const { getAll, deleteAll } = require("../server/handler/alldata");

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
                output: 'stream'
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
                output: 'stream'
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
                output: 'stream'
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
                output: 'stream'
            },
        },
    },

    // deleteArticle - Menghapus Data Artikel dari Firestore
    {
        method: "DELETE",
        path: "/articles/{id}",
        handler: deleteArticle,
    },
];

// Export Routes
module.exports = routes;
