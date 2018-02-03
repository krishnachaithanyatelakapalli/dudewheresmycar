var fs = require('fs'),
    html = fs.readFileSync('index.html'),
    url = require('url'),
    http = require('http'),
    MongoClient = require('mongodb').MongoClient,
    co = require('co');

const port = process.env.PORT || 5000,
      DBUrl = process.env.DATABASEURI || 'mongodb://parkapp:parkapp@ds117311.mlab.com:17311/parkingdb',
      coll_name = process.env.COLL_NAME || 'parking_locations';

var server = http.createServer(function (req, res) {
    var reqUrl = [];
    reqUrl = req.url.toString().split("?");
    if (req.method === 'POST') {
	var body = [];
        req.on('data', function(chunk) {
            body.push(chunk);
        }).on('end', function() {
	    //url: serverurl/addParking?lat=$lat&long=$long&stat=$status&tag=$name
            if (reqUrl[0] == '/addParking') {
		body = Buffer.concat(body).toString();	
		var urlParams = require('url').parse(req.url, true).query;

		if ( urlParams.tag != null && urlParams.lat != null && urlParams.long != null && urlParams.stat != null) {
		    co( function* () {
		        var db = yield MongoClient.connect(DBUrl);

			var t = yield db.collection(coll_name).find({_locName: urlParams.tag}).count();

			if (t == 0) {
		            var r = yield db.collection(coll_name).insertOne({
			        _locName: urlParams.tag,
			        _status: urlParams.stat,
			        _location: {			        	
			            _latitude: urlParams.lat,
			            _longitude: urlParams.long			        
			        }						
		            });

			    res.writeHead(201, 'CREATED', {'Content-Type': 'text/plain'});
		            res.write('Successfully added');
			} else {
			    res.writeHead(200, 'OK', {'Content-Type': 'text/plain'});
			    res.write('Already Exists');
			}
		        db.close();			
		
		    }).catch( function(err) {
		        console.log(err.stack);
		    });
				    
		    res.end();
		} else {
		    res.writeHead(400, 'BAD REQUEST', {'Content-Type': 'text/plain'});
		    res.end();
		}
            } else {
		res.writeHead(400, 'BAD REQUEST', {'Content-Type': 'text/plain'});
		res.end();
	    }            
	}).on('error', function(err) {
	    console.log(err);	
	});

    } else if (req.method === 'PUT') {
	var body = [];
	req.on('data', function(chunk) {
	    body.push(chunk);
	}).on('end', function() {
	    // url: severurl/updateParking?tag=$name&stat=$new_status
	    if (reqUrl[0] == '/updateParking') {
		body = Buffer.concat(body).toString();
		var urlParams = require('url').parse(req.url, true).query;

		if (urlParams.tag != null && urlParams.stat != null) {
		    co( function* () {
		        var db = yield MongoClient.connect(DBUrl);

			var t = yield db.collection(coll_name).find({_locName: urlParams.tag}).count();

			if (t == 1) {
		            var r = yield db.collection(coll_name).update({_locName: urlParams.tag}, {$set:{_status:urlParams.stat}});

			    res.writeHead(200, 'OK', {'Content-Type': 'text/plain'});
		            res.write('Successfully Updated');
			} else {
			    res.writeHead(404, 'NOT FOUND', {'Content-Type': 'text/plain'});
			    res.write('Doesn\'t exists');
			}

		        db.close();
		    }).catch( function (err) {
		        console.log(err.stack);
		    });
		    
		    res.end();
		} else {
		    res.writeHead(400, 'BAD REQUEST', {'Content-Type': 'text/plain'});
		    res.end();
		}
	    } else {
		res.writeHead(400, 'BAD REQUEST', {'Content-Type': 'text/plain'});
		res.end();
	    }
	}).on('error', function(err) {
		console.log(err);
	});
    } else if (req.method === 'DELETE') {
		// Insert code here
    } else {
	// url: severurl/
	if (req.url === '/') {
	    res.write(html);
	    res.end();
	//url: severurl/listParking
	} else if(reqUrl[0] == '/listParking') {
	    
	    co( function* () {
		var list = []
		var db = yield MongoClient.connect(DBUrl);

		var t = yield db.collection(coll_name).count();
	
		if (t > 0) {
		    var r = yield db.collection(coll_name).find().toArray();
		    for (var i = 0, len = r.length; i < len; i++) {
		        var doc = {
			    _locName : r[i]._locName,
			    _status : r[i]._status,
			    _location : r[i]._location
		        };
		        list.push(doc);
		    }
		    fs.writeFile(__dirname + '/park.json', JSON.stringify(list), (err) => {
		        if (err) throw err;
		    });	
		    res.write(JSON.stringify(list);
		    res.end();
		} else {
		    res.writeHead(200, 'OK', {'Content-Type': 'text/plain'});
		    res.write('Database is Empty');
		}	
		db.close();
	    }).catch( function(err) {
		console.log(err.stack);
	    });	

	    fs.readFile(__dirname + '/park.json', 'utf8', function (err, data) {
		res.write(JSON.stringify(data));
		res.end();
	    });
	} else {
	    res.writeHead(404, {'Content-Type': 'text/plain'});
	    res.end();
	}
    }
});
server.listen(port);
console.log('listening at ' + port);
