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

function showReviews() {
    var x = document.getElementById("adminReviewDelete");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function showOwners() {
    var x = document.getElementById("adminTruckDelete");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function showNonOwners() {
    var x = document.getElementById("adminUserDelete");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}