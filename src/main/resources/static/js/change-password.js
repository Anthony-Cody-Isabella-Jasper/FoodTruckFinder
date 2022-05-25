$(document).ready(function () {
	$('[data-toggle="tooltip"]').tooltip();
});
let passwordElement = document.getElementById("password");
let passedNumberReq = false;
let passedUppercaseReq = false;
let passedLowercaseReq = false;
let passedCharCountReq = false;
let buttonElement = document.getElementById("submitButton");
passwordElement.addEventListener("keyup", () => {
	passedNumberReq = /\d/.test(passwordElement.value);
	passedUppercaseReq = false;
	passedLowercaseReq = false;
	passwordElement.value.split("").forEach((char, index) => {
		if (char.toUpperCase() === char && char.charCodeAt(0) > 64 && char.charCodeAt(0) < 91) {
			passedUppercaseReq = true;
		}
		if (char.toLowerCase() === char && char.charCodeAt(0) > 96 && char.charCodeAt(0) < 123) {
			passedLowercaseReq = true;
		}
	});
	passedCharCountReq = passwordElement.value.length > 7;
	if (passedNumberReq) {
		document.getElementById("numeralReq").setAttribute("style", "color: green");
	} else {
		document.getElementById("numeralReq").setAttribute("style", "color: black");
	}
	if (passedUppercaseReq) {
		document.getElementById("uppercaseReq").setAttribute("style", "color: green");
	} else {
		document.getElementById("uppercaseReq").setAttribute("style", "color: black");
	}
	if (passedLowercaseReq) {
		document.getElementById("lowercaseReq").setAttribute("style", "color: green");
	} else {
		document.getElementById("lowercaseReq").setAttribute("style", "color: black");
	}
	if (passedCharCountReq) {
		document.getElementById("charCountReq").setAttribute("style", "color: green");
	} else {
		document.getElementById("charCountReq").setAttribute("style", "color: black");
	}
	if (passedNumberReq && passedCharCountReq && passedUppercaseReq && passedLowercaseReq) {
		buttonElement.disabled = false;
		buttonElement.value = "Change Password";
	} else {
		buttonElement.disabled = true;
		buttonElement.value = "Inadequate Password";
	}
});