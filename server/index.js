// Dependencies
const Hapi = require("@hapi/hapi");
const routes = require("./routes.js");
const firebase_admin = require("firebase-admin");
const serviceAccount = require("./private/firebase.json");

// Fungsi Memulai Firebase
const firebase_init = () => {
    firebase_admin.initializeApp({
        credential: firebase_admin.credential.cert(serviceAccount),
    });
};

// Fungsi Memulai Server
const init = async () => {
    const server = Hapi.server({
        port: process.env.PORT || 8080,
        host: "0.0.0.0"
    });
    server.route(routes);
    await server.start();
    console.log(`Server: ${server.info.uri}`);
};

// Mengeksekusi Fungsi
firebase_init();
init();