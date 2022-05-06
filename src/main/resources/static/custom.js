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


function showProfPics() {
    if (document.getElementById("truckCheck").checked === true) {
        document.getElementById("avatar-choices").style.visibility = "hidden";
    } else if(document.getElementById("userCheck").checked === true){
        document.getElementById("avatar-choices").style.visibility = "visible";
    }
}