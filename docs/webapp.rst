Web Application
-------------------------------------------

.. toctree::
   :maxdepth: 2

   web-register
   web-login
   web-differ-anonymous
   web-differ-registered


The main purpose of this tool is to compare two images and calculate
differences between them. 
It also lists various parameters of those images, even in case when only one image file is selected.

The online application *DIFFER* is utilizing existing tools :

- *JHOVE*
- *FITS*
- *ExifTool*
- *KDU_expand*
- *DJVUDUMP*
- *Jpylyzer*
- *imagemagick*
-  etc.

Those tools are mainly used separately across a whole spectrum of existing projects.

This open source application comes with a well-structured and uniform GUI, which helps the user to understand the relationships between various file format properties, detect visual and non-visual errors and simplifies decision-making. An additional feature called compliance-check is designed to help us check the required specifications of the *JPEG2000* file format.

This application supports selected image formats: 

   **JPEG/JFIF**::

       (*.jfi, *.jfif, *.jif, *.jpg, *.jpe,*.jpeg)

   **JPEG2000**::

       (*.j2k, *.jpm, *.jp2, *.jpf, *. jpx, *.mj2)

   **TIFF**::

       (*.tif, *.tiff), DjVu

   **sDjVu**:: 

       (*.djv, *.djvu) 

   **PNG**::
      
      (*.png)

   **PDF**::
   
      (*.pdf)

.. note::

   Application offers a way to compare images. If you use it as
   anonymous, images you compare can not be larger than `5MB`

   If you log into the application you can compare images without any restriction.

   You can use profiles if you log into the application.
