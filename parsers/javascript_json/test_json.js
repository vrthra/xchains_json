var fs = require('fs');
var path = process.argv[2];

try {
	var data = fs.readFileSync(path);
	var json = JSON.parse(data);
	//console.log(json)
	console.log("valid");
	process.exit(0);
} catch (e) {
	console.log("--", e.message);
	if (e.message.includes("Unexpected token") || e.message.includes("Unexpected number") || e.message.includes("Unexpected string")) {
		console.log("invalid");
		process.exit(2);
	}
	console.log("incomplete");
	process.exit(1);
}
process.exit(0);
