"use strict";
let changeCursor = () => {
	document.body.classList.add("konami");
}
let keypressArray = [];
let code = ['arrowup', 'arrowup', 'arrowdown', 'arrowdown', 'arrowleft', 'arrowright', 'arrowleft', 'arrowright', 'b', 'a'];
let success = false;
document.addEventListener("keyup", event => {
	keypressArray.push(event.key.toLowerCase());
	keypressArray.forEach((key, index) => {
		if (key === code[index]) {
			success = true;
		} else {
			success = false;
			keypressArray.length = 0;
		}
	});
	if (success && keypressArray.length === 10) {
		changeCursor();
	}
});