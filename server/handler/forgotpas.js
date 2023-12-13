const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const nodemailer = require('nodemailer');

const forgotPassword = async (request, h) => {
    const apiKey = request.headers["x-api-key"];

    if (apiKey !== api_key) {
        return h.response({
            status: "unauthorized",
            message: "Invalid API key",
        }).code(401);
    }

    try {
        const { users_email } = request.payload;

        // Check if the user with the provided email exists
        const userRecord = await firebase_admin.auth().getUserByEmail(users_email);

        // Generate a password reset link
        const resetLink = await firebase_admin.auth().generatePasswordResetLink(users_email);

        // TODO: Implement your email sending logic here
        // You should send the reset link to the user's email using a service like Nodemailer or a third-party email provider

        // Placeholder response
        const response = h.response({
            status: "success",
            message: "Password reset link sent to the user's email",
            resetLink, // Shorthand for resetLink: resetLink
        }).code(200);

        return response;
    } catch (error) {
        console.error("Error in forgotPassword:", error);

        const response = h.response({
            status: "bad request",
            message: "Error sending password reset link",
        }).code(500); // Internal Server Error

        return response;
    }
};

module.exports = { forgotPassword, };
