const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");

const loginUsers = async (request, h) => {
    const key = request.headers["x-api-key"];

    if (key !== api_key) {
        return h.response({
            status: "unauthorized",
        }).code(401);
    }

    try {
        const { users_email, users_password } = request.payload;
        const userRecord = await firebase_admin.auth().getUserByEmail(users_email);

        // Perform password validation here (compare users_password with the stored hash)
        // For security, use a secure password hashing library like bcrypt

        // Placeholder for password validation, replace with your implementation
        const isPasswordValid = true; // Replace this with actual validation

        if (!isPasswordValid) {
            return h.response({
                status: "bad request",
                message: "Invalid email or password",
            }).code(401); // Unauthorized status code
        }

        // If the password is valid, generate a custom token
        const idToken = await firebase_admin.auth().createCustomToken(userRecord.uid);

        const responseData = {
            users_id: userRecord.uid,
            status: "success",
            token: idToken,
        };

        const response = h.response(responseData).code(200);
        return response;
    } catch (error) {
        console.error("Error in loginUsers:", error);

        const response = h.response({
            status: "bad request",
            message: "Invalid email or password",
        }).code(401); // Unauthorized status code

        return response;
    }
};



module.exports = { loginUsers, };
