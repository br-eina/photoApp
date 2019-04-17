# photoApp

### 1. TAKE PHOTO AND SAVE
* Requesting permissions for camera and access to the external storage
* Sending intent for device camera app
* Automatically saving the image to *picture/* directory (**to_fix**)

### 2. OPEN GALLERY
* Sending intent to any picture provider (including gallery)
* Showing selected image in ImageView through *picasso* (resize)

### 3. ROTATE THE IMAGE
* Rotating the image in ImageView clockwise (**TODO: custom rotation**)

### 4. SHOW IM
* Showing big image from *drawable/* directory in the source of app
* Resizing it to fit in ImageView (**TODO: refactor to *picasso* method**)

### 5. SAVE
* Saving image from ImageView to *internal_storage/saved_images/ * directory 
* Naming the file as *IMG_TIMESTAMP.jpg* 

### 6. SEND
* Sending image from ImageView to **postGet** activity
* Displaying it in ImageView in **postGet** activity

### 7. X
* Displaying processed image in ImageView


# postget

### 1. PUT REQUEST
* Encoding image from ImageView (which was sent from **MainActivity**) to base64 string
* Sending it to server in `{"value" : "base64_string"}` format

### 2. INITIATE PROCESSING
* Sending filler in `{"value" : "smth"}` format
* That filler is initiating function to proccess the image

### 3. GET REQUEST
* Requesting base64 string from server in `{"value" : "base64_string"}` format
* Decoding it
* Showing decoded string (image) in ImageView

### 4. SEND BACK
* Sending processed image from ImageView to **MainActivity** 

### Steps of processing: 
* Decoding base64 string from client to *.jpg*
* Processing image (**look *form_processing***)
* Encoding *.jpg* image to base64 string
