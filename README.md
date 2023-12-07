![Logo](https://cdn.discordapp.com/attachments/964505604657909760/1182373152379125770/Baner.png?ex=658475ce&is=657200ce&hm=15feec976516933bcbb6d9149ae59a3e1c7378ed8599d44574b6ccd71c5573fa&)

![Logo](https://cdn.discordapp.com/attachments/964505604657909760/1175754312199184484/Frame_3.png?ex=656c6188&is=6559ec88&hm=3e7eb6424c9cc12da307cb016445e1fbc97f1d557fac0aa268dadddfced3de40&)


# Parental Peace : Reduce Baby Blues by Guidance and Consultation


"Parental Peace" adalah aplikasi mobile yang dirancang khusus untuk mendukung orangtua baru dalam perawatan bayi mereka. Aplikasi ini menyediakan berbagai fitur yang memungkinkan pengguna untuk merespons kebutuhan bayi dengan lebih baik, meningkatkan koneksi emosional antara orangtua dan bayi, dan memberikan sumber daya informatif tentang perawatan bayi.

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

## All Items

#### Get all items

```
  GET {{server}}/
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Delete item

```
  DEL {{server}}/
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |


###
## Users
#### Get All Users
```
  GET {{server}}/users
```
#### Get All Users by Id
```
  GET {{server}}/users/{{idUser}}
```
#### Add Users
```
  POST {{server}}/users
```
#### Update Users by Id
```
  PUT {{server}}/users/{{idUser}}
```
#### Delete Users by Id
```
  DEL {{server}}/users/{{idUser}}
```
###
## Articel
#### Get All Articel
```
  GET {{server}}/Articel
```
#### Get All Articel by Id
```
  GET {{server}}/Articel/{{idArticel}}
```
#### Add Articel
```
  POST {{server}}/Articel
```
#### Update Articel by Id
```
  PUT {{server}}/Articel/{{idArticel}}
```
#### Delete Articel by Id
```
  DEL {{server}}/Articel/{{idArticel}}
```
###

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
