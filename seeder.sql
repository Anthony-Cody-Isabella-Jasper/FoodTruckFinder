use food_truck_finder_db;

insert into users (admin, email, password, profile_picture, truck_owner, username) values (false, 'email@email.com', 'pass', null, false, 'User1');

insert into users (admin, email, password, profile_picture, truck_owner, username) values (false, 'email2@email.com', 'pass', null, true, 'User2');

insert into trucks (description, latitude, longitude, location_confirmation, name, phone, profile_picture, truck_owner_id) values ('new food truck on the block', 75, 75, false,'FoodyTruck', '789-543-5432', 'https://smallbiztrends.com/ezoimgfmt/media.smallbiztrends.com/2022/02/food-truck-finder-850x476.png?ezimgfmt=ng%3Awebp%2Fngcb12%2Frs%3Adevice%2Frscb12-1', 2);

insert into truck_pictures (picture, truck_id) VALUES ('https://mobile-cuisine.com/wp-content/uploads/2012/12/Good-Dog-Houston-Menu-e1575380234864.jpg', 1);

insert into cuisines (name)
values ('american');

insert into trucks_cuisines (truck_id, cuisine_id) VALUES (1, 1);

insert into menu_items (description, name, price, vegan, vegetarian, truck_id) VALUES ('whole ass hotdog', 'doggo', 13.99, false, false, 1);

insert into reviews (rating, review, truck_id, user_id) VALUES (4, 'Just some quality dogs at ridiculous prices',1, 1);

insert into user_confirmations (user_id, truck_id) VALUES (1, 1);

insert into user_favorites (user_id, truck_id) VALUES (1, 1);
