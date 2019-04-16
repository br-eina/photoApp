# photoApp

### 1. TAKE PHOTO AND SAVE
* Requesting permissions for camera and access to the external storage
* Sending intent for device camera app
* Automatically saving the image to *picture/* directory (**to_fix**)

### 2. OPEN GALLERY
* Sending intent to any picture provider (including gallery)
* Showing selected image in ImageView (**fails with big images, needs resizing**)

### 3. ROTATE THE IMAGE
* Rotating the image in ImageView clockwise (**TODO: custom rotation**)

### 4. SHOW IM
* Showing big image from *drawable/* directory in the source of app
* Resizing it to fit in ImageView (**TODO: refactor to *picasso* method**)

### 5. SAVE
* Saving image from ImageView to *internal_storage/saved_images/ * directory 
* Naming the file as *shutta_TIMESTAMP.jpg* (**TODO: remake to normal name**)

### 6. PICASSO
* Opening the image from certain folder (**TODO: make intent to pick the image**)
* Resizing it to certain size and displaying in ImageView 
