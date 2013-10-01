Separate long running jobs from web actions
---------------------------------------------------------------------------------------------------------------------------

The intention is to run the external processes in a separate 
JVM to be more robust and to offer standard integration pattern.

Since application calls a lot of external applications extracting of all significant properties takes a long time.
And when uploading images to web often stops with error because of that.

It is necessary to split uploading of images into two or three parts:

   1) uploading images into web appliacation
   2) sending of a request to process uploaded images
   3) listening of a progress of a processed job

There are a few solutions:

   1) upload images and after it finishes call external service to process uploaded images. 
      Progress of a job will be taken from communication with a service.
   2) upload images and after it finishes send message to RabbitMQ to process uploaded images.
      Progress of a job will be taken from a queue that informs about progress.

