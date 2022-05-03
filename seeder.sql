use food_truck_finder_db;

insert into users (admin, email, password, profile_picture, truck_owner, username) values (false, 'email@email.com', 'pass', 'https://cdn5.vectorstock.com/i/1000x1000/73/04/female-avatar-profile-icon-round-woman-face-vector-18307304.jpg', false, 'User1');
insert into users (admin, email, password, profile_picture, truck_owner, username) values (false, 'email2@email.com', 'pass', 'https://cdn5.vectorstock.com/i/1000x1000/72/74/female-avatar-profile-icon-round-woman-face-vector-18307274.jpg', false, 'User2');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email3@email.com', 'pass', 'https://i.pinimg.com/564x/a6/58/32/a65832155622ac173337874f02b218fb--people-icon-avatar.jpg', false, 'User3');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email4@email.com', 'pass', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMx1itTXTXLB8p4ALTTL8mUPa9TFN_m9h5VQ&usqp=CAU', false, 'User4');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email5@email.com', 'pass', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmE--NjTTosid1jFeMFdc12EVQtKi7XPRMYqeHI0_4jJlqBanUiyQ-KrqN5tsdK_MO0j8&usqp=CAU', true, 'User5');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email6@email.com', 'pass', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRv3v8eT0Vp-0n3xYiISR6P-5Q1GZ0zoKHUQbXd-66yiWE2W4mmpO5LQfk0tUnpAvo54KU&usqp=CAU', true, 'User6');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email7@email.com', 'pass', 'https://thumbs.dreamstime.com/b/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-46846330.jpg', true, 'User7');
insert into users (admin, email, password, profile_picture, truck_owner, username) VALUES (false, 'email8@email.com', 'pass', 'https://static.vecteezy.com/system/resources/previews/002/275/847/original/male-avatar-profile-icon-of-smiling-caucasian-man-vector.jpg', true, 'User8');


insert into trucks (description, latitude, location_confirmation, longitude, name, phone, profile_picture, truck_owner_id) values ('new food truck on the block', 30, false, 98 ,'FoodyTruck', '789-543-5432', 'https://smallbiztrends.com/ezoimgfmt/media.smallbiztrends.com/2022/02/food-truck-finder-850x476.png?ezimgfmt=ng%3Awebp%2Fngcb12%2Frs%3Adevice%2Frscb12-1', 5);
insert into trucks (description, latitude, location_confirmation, longitude, name, phone, profile_picture, truck_owner_id) values ('We have food here?', 28, false, 95 ,'TruckWfoods', '125-475-4758', 'https://www.thebalancesmb.com/thmb/IRZI2gmNsYFTTPCH774ohMD4uJE=/2088x1436/filters:fill(auto,1)/food-truck-in-the-street-496731672-863bfb69328341c1804fec18e39be715.jpg', 6);
insert into trucks (description, latitude, location_confirmation, longitude, name, phone, profile_picture, truck_owner_id) values ('Lets eat!', 27, false, 90 ,'GoneCookin', '364-475-8412', 'https://d1m6300l53o0vp.cloudfront.net/wp-content/uploads/2019/11/NicoleMlakar_SAM_FoodTrucks_072413_069.jpg', 7);
insert into trucks (description, latitude, location_confirmation, longitude, name, phone, profile_picture, truck_owner_id) values ('BBQ 4 dayz', 32, false, 92 ,'HereWeeeGo', '978-854-3791', 'https://cloudfront-us-east-1.images.arcpublishing.com/gray/KE63ZKXDSJDC7KRYIUO6KWN7GY.jpg', 8);


insert into truck_pictures (picture, truck_id) VALUES ('https://mobile-cuisine.com/wp-content/uploads/2012/12/Good-Dog-Houston-Menu-e1575380234864.jpg', 1);
insert into truck_pictures (picture, truck_id) VALUES ('https://f5d8k6r5.stackpathcdn.com/wp-content/uploads/2019/07/FoodTruck-Menus-NEW2019-Web.jpg', 2);
insert into truck_pictures (picture, truck_id) VALUES ('https://image.shutterstock.com/image-vector/food-truck-menu-street-festival-600w-747123034.jpg', 3);
insert into truck_pictures (picture, truck_id) VALUES ('https://i.pinimg.com/originals/db/07/19/db071981cc83f55ad9b62c9168b8cc08.jpg', 4);


insert into cuisines (name)
values ('american');
insert into cuisines (name)
values ('burgers');
insert into cuisines (name)
values ('mexican');
insert into cuisines (name)
values ('indian');



insert into trucks_cuisines (truck_id, cuisine_id) VALUES (1, 1);
insert into trucks_cuisines (truck_id, cuisine_id) VALUES (2, 2);
insert into trucks_cuisines (truck_id, cuisine_id) VALUES (3, 3);
insert into trucks_cuisines (truck_id, cuisine_id) VALUES (4, 4);


insert into menu_items (description, name, price, vegan, vegetarian, truck_id) VALUES ('whole ass hotdog', 'doggo', 13.85, false, false, 1);
insert into menu_items (description, name, price, vegan, vegetarian, truck_id) VALUES ('5lb patty', 'Big Boy Burger', 21.99, false, true, 2);
insert into menu_items (description, name, price, vegan, vegetarian, truck_id) VALUES ('it has sugar', 'Churro', 3.75, true, false, 3);
insert into menu_items (description, name, price, vegan, vegetarian, truck_id) VALUES ('Steak, Chicken or Shrimp', 'Taco Taco', 39.36, true, false, 4);


insert into reviews (rating, review, truck_id, user_id) VALUES (4, 'Just some quality dogs at ridiculous prices',1, 1);
insert into reviews (rating, review, truck_id, user_id) VALUES (1, 'Too big',2, 2);
insert into reviews (rating, review, truck_id, user_id) VALUES (5, 'I mean, sugar is cool',3, 3);
insert into reviews (rating, review, truck_id, user_id) VALUES (2, 'Get you some tacos here!',4, 4);


insert into user_confirmations (user_id, truck_id) VALUES (1, 1);
insert into user_confirmations (user_id, truck_id) VALUES (2, 2);
insert into user_confirmations (user_id, truck_id) VALUES (3, 3);
insert into user_confirmations (user_id, truck_id) VALUES (4, 4);


insert into user_favorites (user_id, truck_id) VALUES (1, 1);
insert into user_favorites (user_id, truck_id) VALUES (1, 2);
insert into user_favorites (user_id, truck_id) VALUES (2, 2);
insert into user_favorites (user_id, truck_id) VALUES (2, 3);
insert into user_favorites (user_id, truck_id) VALUES (3, 3);
insert into user_favorites (user_id, truck_id) VALUES (3, 1);
insert into user_favorites (user_id, truck_id) VALUES (4, 4);
insert into user_favorites (user_id, truck_id) VALUES (4, 2);
