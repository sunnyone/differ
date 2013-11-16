Compare
................................................

.. note:: 

   When processing images you can see box with progress of
   processing. 
   You can view what extractors are being used.

For each window you can see:

- **image with histogram**
- **metadata table**


.. note:: 

   They are the same as for Proceed.


Metadata table
~~~~~~~~~~~~~~~~~~~~~~~~~~~

The table describes *properties* of an image.

.. list-table:: 
   :header-rows: 0

   * - **key**
     - name of a property

   * - **source**
     - name of a proper extractor

   * - **value**
     - value of a property

   * - **unit**
     - unit of a property

Metadata table is splitted into four sections:

- **used extractors**:

  extractors that were used to get properties.

- **identification**:

  properties regarding to image file. And basic properties.

- **validation**:

  informations about format of an image. If an image format is
  corrupted, ...

- **characterization**:

  informations regarding to image. What colors were used. Color depth,
  ...


- **others**
  
  More informations with various importance.

Lines can be drawn with color:

- **red**:
  
  If a property of the first image differs than the property of the second image.

.. note::

   You can click on a table header to sort lines.

Lines with property values can be drawn with color:

- **red**:
  
  There are more values for the same property. So some extractor
  provided other value than other one.

- **green**:

  Both images has the same value or a given property.
