const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");


// getAllArticles - Mengambil Semua Data Artikel dari Firestore
const getAllArticles = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        // Mengambil semua data artikel dari Firestore
        const snapshot = await db.collection('articles').get();

        const articles = [];
        snapshot.forEach((doc) => {
            articles.push({
                articleId: doc.id,
                ...doc.data(),
            });
        });

        const response = h.response({
            status: "success",
            data: articles,
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error getting article data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to get article data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

// getArticle - Ambil Data Artikel Tertentu
const getArticle = async (request, h) => {
    // Mengambil Kunci API dari Request Header
    const key = request.headers["x-api-key"];
    // Jika Kunci API Benar
    if (key === api_key) {
        // Mengambil ID Artikel dari Request Params
        const { id } = request.params;
        const responseData = {};

        const db = firebase_admin.firestore();
        responseData[id] = (await db.collection("articles").doc(id).get()).data();

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

// addArticle - Menyimpan Data Artikel ke Firestore
const addArticle = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        const storage = new Storage({
            keyFilename: path.join(__dirname, "../private/gcloud.json"),
        });
        const bucketName = require("../private/key.json").storage_bucket;

        // Mendapatkan data artikel dari payload
        const {
            title,
            content,
            article_picture,  // Assuming article_picture is included in the payload
        } = request.payload;

        // Generate a unique ID for the article
        const articleId = "a" + Date.now().toString();

        // Define a specific temporary directory for file upload
        const tempDirectory = path.join(__dirname, "temp");

        // If the 'temp' directory doesn't exist, create it
        if (!fs.existsSync(tempDirectory)) {
            fs.mkdirSync(tempDirectory);
        }

        // If there's an article picture, upload it to cloud storage
        let pictureUrl = null;
        if (article_picture) {
            const filename = article_picture.hapi.filename;
            const data = article_picture._data;

            // The path to your file to upload
            const filePath = path.join(tempDirectory, filename);

            // Write the file using promises
            await fs.promises.writeFile(filePath, data);

            // The new ID for your GCS file
            const destFileName = `articles/${articleId}/${filename}`;

            // File URL
            pictureUrl = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

            // Upload the file to cloud storage
            const options = {
                destination: destFileName,
            };

            await storage.bucket(bucketName).upload(filePath, options);

            // Making the file public to the internet
            await storage.bucket(bucketName).file(destFileName).makePublic();

            // Delete the temporary file after upload
            await fs.promises.unlink(filePath);
        }

        // Menyimpan data artikel ke Firestore
        await db.collection('articles').doc(articleId).set({
            title: title,
            content: content,
            article_picture: pictureUrl,
        });

        const response = h.response({
            status: "success",
            message: "Article data has been successfully saved.",
            data: {
                articleId: articleId,
            },
        });

        response.code(201); // 201 Created
        return response;
    } catch (error) {
        console.error("Error saving article data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to save article data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};



// updateArticle - Memperbarui Data Artikel di Firestore
const updateArticle = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        const storage = new Storage({
            keyFilename: path.join(__dirname, "../private/gcloud.json"),
        });
        const bucketName = require("../private/key.json").storage_bucket;

        const articleId = request.params.id;
        const updatedArticleData = request.payload;

        // Define a specific temporary directory for file upload
        const tempDirectory = path.join(__dirname, "temp");

        // If the 'temp' directory doesn't exist, create it
        if (!fs.existsSync(tempDirectory)) {
            fs.mkdirSync(tempDirectory);
        }

        // If there's an updated article picture, upload it to cloud storage
        let pictureUrl = null;
        if (updatedArticleData.article_picture) {
            const filename = updatedArticleData.article_picture.hapi.filename;
            const data = updatedArticleData.article_picture._data;

            // The path to your file to upload
            const filePath = path.join(tempDirectory, filename);

            // Write the file using promises
            await fs.promises.writeFile(filePath, data);

            // The new ID for your GCS file
            const destFileName = `articles/${articleId}/${filename}`;

            // File URL
            pictureUrl = `https://storage.googleapis.com/${bucketName}/${destFileName}`;

            // Upload the file to cloud storage
            const options = {
                destination: destFileName,
            };

            await storage.bucket(bucketName).upload(filePath, options);

            // Making the file public to the internet
            await storage.bucket(bucketName).file(destFileName).makePublic();

            // Delete the temporary file after upload
            await fs.promises.unlink(filePath);
        }

        // Update the article data in Firestore
        const updateData = {
            ...updatedArticleData,
            article_picture: pictureUrl,
        };

        await db.collection('articles').doc(articleId).update(updateData);

        const response = h.response({
            status: "success",
            message: "Article data has been successfully updated.",
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error updating article data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to update article data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

const deleteArticle = async (request, h) => {
    try {
        const db = firebase_admin.firestore();
        const storage = new Storage({
            keyFilename: path.join(__dirname, "../private/gcloud.json"),
        });

        const articleId = request.params.id;

        // Get the article data
        const articleRef = db.collection('articles').doc(articleId);
        const articleSnapshot = await articleRef.get();

        if (!articleSnapshot.exists) {
            const response = h.response({
                status: "not found",
                message: "Article not found",
            });
            response.code(404); // Not Found
            return response;
        }

        const articleData = articleSnapshot.data();
        const imageUrl = articleData.article_picture;

        // Delete the article from Firestore
        await articleRef.delete();

        // Delete the associated image from cloud storage
        const fileName = imageUrl.split('/').pop();
        const bucket = storage.bucket(bucketName);
        const file = bucket.file(`articles/${articleId}/${fileName}`);
        
        await file.delete();

        const response = h.response({
            status: "success",
            message: "Article data and associated image have been successfully deleted.",
        });

        response.code(200); // 200 OK
        return response;
    } catch (error) {
        console.error("Error deleting article data:", error);

        const response = h.response({
            status: "error",
            message: "Failed to delete article data.",
        });

        response.code(500); // 500 Internal Server Error
        return response;
    }
};

module.exports = {
    getAllArticles,
    getArticle,
    addArticle,
    updateArticle,
    deleteArticle,
};