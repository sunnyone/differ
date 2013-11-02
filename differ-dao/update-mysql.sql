rename table users to user;
rename table images to image;
alter table user change admin is_admin tinyint(1);
alter table image change size file_size int(11);
