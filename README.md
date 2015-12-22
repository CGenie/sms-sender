# SMS Sender - the stupid HTTP SMS gateway

## Turn your Android phone into an SMS gateway.

Install the `sms-sender` app and your phone will become a HTTP-driven SMS
sending gateway (by default on port `8080`).

The HTTP server is very simple and is supposed to be that way -- to handle more complex
situations write your own web server and connect the app to it.

The following endpoints are supported:

* `[POST] /sms/send/`: send SMS. The body is of the form:
  `{"source": "your-phone-number", "destination": "destination-number", "text": "SMS content"}`
* `[POST] /sms/webhooks/add/`: add a user-defined HTTP callback. The body is of the form:
  `{"endpoint": "http://your.server"}`. When SMS arrives to the phone, all registered endpoints
  will receive a `POST` HTTP request with the following body:
  `{"source": "sms-senders-number", "text": "sms content"}`
* `[GET] /sms/webhooks/list/`: list all registered webhooks.
