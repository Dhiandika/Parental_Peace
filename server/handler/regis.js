
const firebase_admin = require("firebase-admin");
const api_key = require("../private/key.json").api_key;
const  {Storage}= require("@google-cloud/storage");
const bucketName = require("../private/key.json").storage_bucket;
const fs = require("fs");
const path = require("path");
const util = require('util');
const bcrypt = require('bcrypt');

// Function to register a new user
const registerUser = async (request, h) => {
    try {
        const { username, email, phone, password } = request.payload;

        // Hash the password before storing it
        const hashedPassword = await bcrypt.hash(password, 10); // Increase the salt rounds for better security

        // Check if the email is already in use
        const userExists = await isUserExists(email);

        if (userExists) {
            const response = h.response({
                error: true,
                message: 'Email address is already in use',
            });
            response.code(400); // Bad Request
            return response;
        }

        // Create user in Firebase Authentication using the hashed password
        const userRecord = await firebase_admin.auth().createUser({
            email: email,
            password: hashedPassword,
        });

        const db = firebase_admin.firestore();
        const usersCollection = db.collection('users');

        // Create a new document for the user in Firestore
        const newDocumentRef = usersCollection.doc();
        const documentId = newDocumentRef.id;

        await newDocumentRef.set({
            user_id: documentId,
            username: username,
            email: email,
            phone: phone,
            password: hashedPassword,
            firebase_uid: userRecord.uid,
        });

        const response = h.response({
            error: false,
            message: 'Registration successful',
        });
        response.code(201); // Created
        return response;
    } catch (error) {
        console.error('Error registering user:', error);

        const response = h.response({
            error: true,
            message: 'Internal Server Error',
        });
        response.code(500); // Internal Server Error
        return response;
    }
};

// Function to check if a user with the given email already exists
const isUserExists = async (email) => {
    const db = firebase_admin.firestore();
    const usersCollection = db.collection('users');
    const querySnapshot = await usersCollection.where('email', '==', email).get();

    return !querySnapshot.empty;
};

module.exports = {
    registerUser,
};