    <!DOCTYPE html>
    <html>
      <head>
        <title>Hello Client Monkey 4</title>
        <script type="text/javascript"
          src="//media.twiliocdn.com/sdk/js/client/v1.3/twilio.min.js"></script>
        <script type="text/javascript"
          src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js">
        </script>
        <link href="//static0.twilio.com/resources/quickstart/client.css"
          type="text/css" rel="stylesheet" />
        <script type="text/javascript">
        
        var connecti;
        
        Twilio.Device.setup("${token}", {debug: true});

          Twilio.Device.ready(function (device) {
            $("#log").text("Ready");
          });

          Twilio.Device.error(function (error) {
            $("#log").text("Error: " + error.message);
          });

          Twilio.Device.connect(function (conn) {
        	  connecti = conn;
            $("#log").text("Successfully established call");
          });

          Twilio.Device.disconnect(function (conn) {
            $("#log").text("Call ended");
          });

          Twilio.Device.incoming(function (conn) {
            $("#log").text("Incoming connection from " + conn.parameters.From);
            // accept the incoming connection and start two-way audio
            conn.accept();
          });
          
          function sendDigits(dig)
          {
        	  connecti.sendDigits(dig.value);
        	  return false;
          }
          
          function call() {
            // get the phone number to connect the call to
            params = {"PhoneNumber": $("#number").val()};
            Twilio.Device.connect(params);
            console.log(params);
          }

          function hangup() {
            Twilio.Device.disconnectAll();
          }
        </script>
      </head>
      <body>
        <button class="call" onclick="call();">
          Call
        </button>

        <button class="hangup" onclick="hangup();">
          Hangup
        </button>

        <input type="text" id="number" name="number"
          placeholder="Enter a phone number to call"/>

        <div id="log">Loading pigeons...</div>
        <div style="height:210px; width:280px;margin:auto;">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="1" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="2" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="3" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="4" onclick="sendDigits(this);">
        <br>
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="5" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="6" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="7" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="8" onclick="sendDigits(this);">
        <br>
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="9" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="*" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="0" onclick="sendDigits(this);">
        <input type="button" style="width:60px;height:60px;border-radius:60px;font-size:130%;padding:2px;float:left;margin:5px;" value="#" onclick="sendDigits(this);">
        </div>
        
      </body>
    </html>