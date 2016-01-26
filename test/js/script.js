alert( 'Я – Подключенный JavaScript!' );

var title ='HELLO, WHAT IS YOUR NAME';
var nameDefault = 'Enter Name';
var result=null;
 result = prompt(title, nameDefault);

if (result == null) {
	alert('Вы не ввели имя');
} else {
	alert(result);
}

function sum(a, b) {
  return a + b;
}
alert(sum(2,5));
