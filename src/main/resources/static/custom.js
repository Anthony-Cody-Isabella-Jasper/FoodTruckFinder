let avatars = Array.from(document.getElementsByClassName("avatar"));
let fetchedPictures = [];
$.ajax({
    url: "https://randomuser.me/api/?lego&results=" + 50,
    dataType: "json",
    cache: false,
    success: function (data) {
        data.results.forEach(result => {
            if(!fetchedPictures.includes(result.picture.thumbnail)) {
                fetchedPictures.push(result.picture.thumbnail);
            }
        });
        avatars.forEach((picture, index) => {
            picture.firstChild.src = fetchedPictures[index];
            picture.lastChild.value = fetchedPictures[index];
        });
    }
});

// let html = '<p>After confirmation, you can edit your trucks profile</p>'

function showProfPics() {
    if (document.getElementById("truckCheck").checked === true) {
        document.getElementById("avatar-choices").style.visibility = "hidden";
        // document.getElementById("avatar-choices").innerHTML = html;
    } else if(document.getElementById("userCheck").checked === true){
        document.getElementById("avatar-choices").style.visibility = "visible";
    }
}