
## Authors by Team ID: `CH2-PS442`
| Name | Bangkit-ID     | Github-Profile                       |
| :-------- | :------- | :-------------------------------- |
| I Putu Yogi Prasetya Dharmawan      | `M014BSY1124` | [@YogiPrasetyaD](https://github.com/YogiPrasetyaD) |
| Mhd Iqbal Pratama      | `M302BSY0044 ` | [@MhdIqbalPratama](https://github.com/MhdIqbalPratama) |
| Luthfi Dhiya Ramadhan      | `M237BSY0835 ` | [@LuthfiDhiyaRamadhan](https://github.com/LuthfiDhiyaRamadhan) |
| I Putu Dhiandika Aditya Permana      | `C014BSY4164` | [@Dhiandika](https://github.com/Dhiandika) |
| Yoga Pratama      | `C116BSY4186 ` | [@Yoama2](https://github.com/Yoama2) |
| I Wayan Priatna      | `A014BSY2686 ` | [@priatna1054](https://github.com/priatna1054) |

# API Reference
## Endpoint Routes

| Route                           | HTTP Method | Description                                  |
|---------------------------------|-------------|----------------------------------------------|
| /users                          | GET         | Get all users                                |
| /users/{{idUser}}               | GET         | Get users by Id                              |
| /users                          | POST        | Add user                                     |
| /users/{{idUser}}               | PUT         | Update users                                 |
| /users/{{idUser}}               | DEL         | Delete users                                 |
| /laundry                        | GET         | Get all laundry                              |
| /laundry/{{idLaundry}}          | GET         | Get laundry by Id                            |
| /laundry                        | POST        | Add laundry                                  |
| /laundry/{{idLaundry}}          | PUT         | Update laundry                               |
| /laundry/{{idLaundry}}          | DEL         | Delete laundry                               |
| /transaction                    | GET         | Get all transaction                          |
| /transaction/{{idTransaksi}}    | GET         | Get transaction by Id                        |
| /transaction                    | POST        | Add transaction                              |
| /transaction/{{idTransaksi}}    | PUT         | Update transaction                           |
| /transaction/{{idTransaksi}}    | DEL         | Delete transaction                           |

## Endpoints
All requests to the Users API must include the `x-api-key` header with a valid API key.


### 1. Get All Users

#### `GET /users`

Retrieve information about all users.

##### Request

- Method: GET
- Headers:
  - `x-api-key`: Your API Key

##### Response

- Status Code: 200 OK
- Body:
  - `users`: An array of user objects.

##### Example Response

```json
{
    "users": [
        {
            "users_email": "yodya1532626336262@gmail.com",
            "users_role": "user",
            "users_id": "u1701666978475",
            "firebase_uid": "1wFq80ZwfnTgAAju7pKPSuzdqdY2",
            "users_picture": "https://storage.googleapis.com/klinbangkit2023/users/u1701666978475.jpg",
            "users_name": "Yodya Mahesa",
            "users_phone": "08961012345"
        },
        {
            "users_email": "gintingherisanjaya@gmail.com",
            "users_role": "user",
            "users_name": "Heri",
            "users_id": "u1702048723322",
            "users_phone": "12357900099",
            "firebase_uid": "kJdNqMLEPJeAdJVrDTayCIKkKYt2",
            "users_picture": "https://storage.googleapis.com/klinbangkit2023/users/u1702048723322.png"
        }
    ]
}
```

### 2. Get User by ID

#### `GET /users/{id}`

Retrieve information about a specific user identified by their ID.

##### Request

- Method: GET
- Headers:
  - `x-api-key`: Your API Key
- Path Parameters:
  - `id`: The ID of the user

##### Response

- Status Code: 200 OK
- Body: User object

### 3. Create New User

#### `POST /users`

Create a new user.

##### Request

- Method: POST
- Headers:
  - `x-api-key`: Your API Key
- Body Parameters:
  - `users_name`: Name of the user
  - `users_email`: Email of the user
  - `users_phone`: Phone number of the user
  - `users_role`: Role of the user
  - `users_password`: Password for the user
  - `users_picture`: User profile picture (multipart/form-data)

##### Response

- Status Code: 200 OK
- Body:
  - `status`: "success"

### 4. Update User by ID

#### `PUT /users/{id}`

Update information for a specific user identified by their ID.

##### Request

- Method: PUT
- Headers:
  - `x-api-key`: Your API Key
- Path Parameters:
  - `id`: The ID of the user
- Body Parameters:
  - `users_name`: Updated name of the user
  - `users_email`: Updated email of the user
  - `users_phone`: Updated phone number of the user
  - `users_role`: Updated role of the user
  - `users_picture`: Updated user profile picture (multipart/form-data)

##### Response

- Status Code: 200 OK
- Body:
  - `status`: "success"

### 5. Delete User by ID

#### `DELETE /users/{id}`

Delete a specific user identified by their ID.

#### Request

- Method: DELETE
- Headers:
  - `x-api-key`: Your API Key
- Path Parameters:
  - `id`: The ID of the user

#### Response

- Status Code: 200 OK
- Body:
  - `status`: "success"

## Deploying to Cloud Run
- ### Preconditions
  Before deploying your app to Google Cloud Run, ensure that you meet the following prerequisites:
  - Create a Google Cloud Platform (GCP) account and manage projects.
  - Install and configure the Google Cloud SDK on your local machine.
  Please note that "prerequisites" is a plural noun, so it is more appropriate to use "meet the following prerequisites" instead of "meet the following prerequisite" in this context.

- ### Steps
  - Prepare the application
    Ensure that your application is ready for deployment on Google Cloud Run. This involves conducting local testing and ensuring that the necessary configuration is in place.
  - Create a container image
    Google Cloud Run requires the application to be packaged as a distributable container image. Build container images of your applications using tools like Docker.
  - Upload the container image
    Upload the container image you created to the Google Container Registry (GCR) using the gcloud command. Before proceeding, ensure that you are signed in to the correct Google Cloud Platform (GCP) account.
    Example command to upload a container image:
    ```
    gcloud builds submit --tag gcr.io/[PROJECT-ID]/[IMAGE-NAME]
    ```
  - Deploy to Google Cloud Run
    Use the gcloud run deploy command to deploy your application to Google Cloud Run. Specify the service name, select the uploaded container image, and configure any additional options as necessary.
    Example command to deploy an application to Google Cloud Run:
    ```
    gcloud run deploy [SERVICE-NAME] --image gcr.io/[PROJECT-ID]/[IMAGE-NAME] --platform managed
    ```
  - Accessing the application
    After the deployment process is complete, you will receive a URL that provides access to the deployed application. Utilize this URL to access the app through a web browser or by employing an API testing tool such as cURL or Postman.


## Datasets

Dataset Links of This Project :

- [Dataset 1](#)
- [Dataset 2](#)


## UI/UX Design

- Link Lo-Fi Design : [Prototype](https://www.figma.com/file/stmmEOu6LeH6BLNDg4UP1a/Baby-Blues-Project-(Copy)?type=design&node-id=0%3A1&mode=design&t=ehEoacbj6koN2Xfp-1)
- Link Hi-Fi Design : [Wireframe & Mockup](https://www.figma.com/file/stmmEOu6LeH6BLNDg4UP1a/Baby-Blues-Project-(Copy)?type=design&node-id=302-2&mode=design)
- Link Prototype Design : [Refrensi](https://www.figma.com/file/stmmEOu6LeH6BLNDg4UP1a/Baby-Blues-Project-(Copy)?type=design&node-id=226-2&mode=design)

## Deployment Link APK Waras

Download Link APK Waras:<br>
[Parental Peace APK (Google Drive)](#)


## Languages and Tools:
<p align="left"> <a href="https://developer.android.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/android/android-original-wordmark.svg" alt="android" width="40" height="40"/> </a> <a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="40" height="40"/> </a> <a href="https://www.docker.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original-wordmark.svg" alt="docker" width="40" height="40"/> </a> <a href="https://www.figma.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/figma/figma-icon.svg" alt="figma" width="40" height="40"/> </a> <a href="https://firebase.google.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/firebase/firebase-icon.svg" alt="firebase" width="40" height="40"/> </a> <a href="https://flask.palletsprojects.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/pocoo_flask/pocoo_flask-icon.svg" alt="flask" width="40" height="40"/> </a> <a href="https://cloud.google.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/google_cloud/google_cloud-icon.svg" alt="gcp" width="40" height="40"/> </a> <a href="https://git-scm.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="40" height="40"/> </a> <a href="https://www.w3.org/html/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="html5" width="40" height="40"/> </a> <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/javascript/javascript-original.svg" alt="javascript" width="40" height="40"/> </a> <a href="https://kotlinlang.org" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/kotlinlang/kotlinlang-icon.svg" alt="kotlin" width="40" height="40"/> </a> <a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/getpostman/getpostman-icon.svg" alt="postman" width="40" height="40"/> </a> <a href="https://www.python.org" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/python/python-original.svg" alt="python" width="40" height="40"/> </a> <a href="https://www.tensorflow.org" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/tensorflow/tensorflow-icon.svg" alt="tensorflow" width="40" height="40"/> </a> </p>

###

🔥 Supported By

###

<div align="center">
  <img src="https://lldikti10.id/public/img/informasi/berita/MASTER.png" height="80" alt="kampus merdeka" style="margin-right:20px;"/>
  <img src="https://storage.googleapis.com/kampusmerdeka_kemdikbud_go_id/mitra/mitra_af66db2e-0997-4f52-9cc0-a14412eeeab9.png" height="80" alt="bangkit academy" style="margin-right:left0px;"/>
  
</div>

###
