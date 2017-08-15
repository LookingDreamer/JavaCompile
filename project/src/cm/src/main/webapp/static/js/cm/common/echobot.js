var BOSH_SERVICE = 'http://203.195.141.57:7070/http-bind/';
var connection = null; 
var tFlag = false;

function log(msg)  
{
	$('#log').append('<div></div>').append(document.createTextNode(msg));
}

function onConnect(status)
{
    if (status == Strophe.Status.CONNECTING) {
	log('test is connecting.');
    } else if (status == Strophe.Status.CONNFAIL) {
	log('test failed to connect.');
	$('#loginBtn').get(0).value = 'connect';
    } else if (status == Strophe.Status.DISCONNECTING) {
	log('test is disconnecting.');
    } else if (status == Strophe.Status.DISCONNECTED) {
    	tFlag = false;
	log('test is disconnected.');
	$('#loginBtn').get(0).value = 'connect';
    } else if (status == Strophe.Status.CONNECTED) {
    	tFlag = true;
	log('test is connected.');
	log('Send a message to ' + connection.jid + 
	    ' to talk to me.');

	connection.addHandler(onMessage, null, 'message', null, null,  null); 
	connection.send($pres().tree());

	//var reply = $msg({to: '69@im.lzg.com', from: connection.jid, type: 'chat'})
            //.cnode(Strophe.xmlElement('body',null,'test request =====================>'));
	//connection.send(reply.tree());
	log(connection.jid + '>>>>>>>>>>>: test request =====================>');

    }
}

function onMessage(msg) {
    var to = msg.getAttribute('to');
    var from = msg.getAttribute('from');
    var type = msg.getAttribute('type');
    var elems = msg.getElementsByTagName('body');



    if (type == "chat" && elems.length > 0) {
	var body = elems[0];
	var text1 = Strophe.getText(body);
	alert(text1);
	//log(from + '==>>>>>>>>>>>: ' + Strophe.getText(body)  );
    
	//var reply = $msg({to: from, from: to, type: 'chat'}).cnode(Strophe.copyElement(body));
	//connection.send(reply.tree());

	//log(to + '>>>>>>>>>>>: ' + Strophe.getText(body));
    }

    // we must return true to keep the handler alive.  
    // returning false would remove it after it finishes.
    return true;
}

$(document).ready(function () {
    connection = new Strophe.Connection(BOSH_SERVICE);

    // Uncomment the following lines to spy on the wire traffic.
    //connection.rawInput = function (data) { log('RECV: ' + data); };
    //connection.rawOutput = function (data) { log('SEND: ' + data); };

    // Uncomment the following line to see all the debug output.
    //Strophe.log = function (level, msg) { log('LOG: ' + msg); };


    $('#loginBtn').bind('click', function () {
	var button = $('#loginBtn').get(0);
		if (button.value == 'connect') {
		    //button.value = 'disconnect';
		    alert("denglu1");
		    connection.connect($('#username').get(0).value+'@localhost/cm',
				       '111',
				       onConnect,'','','');
		} else {
			//button.value = 'connect';
		   // alert("denglu2");
		    //connection.disconnect();
		}
    });

    $('#send1').bind('click', function () {
		if (tFlag == true) {
			var reply = $msg({to: 'admin@localhost/Spark 2.7.0', from: connection.jid, type: 'chat'})
		            .cnode(Strophe.xmlElement('body',null,$('#textmsg').get(0).value));
			connection.send(reply.tree());
			log(connection.jid + '>>>>>>>>>>>: ' + $('#textmsg').get(0).value);
		} else {
			log('need connect first');
		}
    });
});
