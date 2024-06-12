## Extra Security Feature CustomCookieFilter

The `CustomCookieFilter` is a filter designed to create a token and place it in a cookie, subsequently validating this token with every request. Specifically, it involves JWT authentication. During the initial login process, a token is generated and placed in a cookie. Each cookie has a timer associated with it, expiring either when the JWT expires or becomes invalid after its first use for a single request. Upon each request, the previously generated token is validated, and a new one is created, replacing the old token in a cookie. An exception to this process occurs during logout, where this filter is skipped.

In the case of multiple requests, old cookies are kept alive for a short time, with a default of 60 seconds. This is implemented to prevent conflicts when the server experiences a delay and is unable to send back a new cookie for the next request. This duration is configurable through the `cookie-timer` property in the application file.

Furthermore, cookies are stored in a cache, and a scheduler is in place to clean this cache. The cleaning schedule can be modified using the `cookie-cache-clean-timer` property, which uses a cron-like expression (`0 * * ? * *` in the provided example).

To implement this functionality, the following line of code should be added to your `WebSecurity` class within the `SecurityFilterChain`:

```java
.addFilterBefore(customCookieFilter, BasicAuthenticationFilter.class)
```

Additionally, the following properties should be added to your application file:

```yaml
qlack.util.jwt.secret: qlack # The secret to sign the JWT. Make sure you override this property in your application.
qlack.util.jwt.validity: 60 # The number of minutes a JWT is valid for.

qlack.util.csrf:
  cookie-name: COOKIE-TOKEN # the name of the cookie
  cookie-timer: 60 # per seconds, the timer for keeping old cookies alive for multiple requests
  cookie-cache-clean-timer: 0 * * ? * * # the time of tge scheduler where we clean cache from non-valid cookies
  login-path: # the uri path of controller for login example: '/users/auth' 
  logout-path: # the uri path of controller for logout example: /users/logout'
```

Ensure these configurations are in place for the filter to function correctly.

In case the token is invalid, a 403 error with the message "Invalid token" will be returned as a response. This should be handled in the frontend of the application in order to display an appropriate message to the user.