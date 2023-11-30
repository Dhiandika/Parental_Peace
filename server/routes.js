// Import dari Handler
const {
    getAll,
    deleteAll,
    getAllUsers,
    addUser,
    updateUser,
    deleteUser,
} = require("./handler");

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
    // getAllUsers - Mengambil Semua Data Pengguna dari Firestore
    {
        method: "GET",
        path: "/users",
        handler: getAllUsers,
    },

    // saveUser - Menyimpan Data Pengguna ke Firestore
    {
        method: "POST",
        path: "/users",
        handler: addUser,
    },

    // updateUser - Memperbarui Data Pengguna di Firestore
    {
        method: "PUT",
        path: "/users/{id}",
        handler: updateUser,
    },

    // deleteUser - Menghapus Data Pengguna dari Firestore
    {
        method: "DELETE",
        path: "/users/{id}",
        handler: deleteUser,
    },
];


// Export Routes
module.exports = routes;