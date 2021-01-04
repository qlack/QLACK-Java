# QLACK Util validation
A Spring `RestControllerAdvice` converting `MethodArgumentNotValidException` and `ConstraintViolationException` validation errors to a common structure.

This utility tries to extract information from your validation errors and normalise them into a structure that can be further utilised by a front-end counterpart to render these errors. QLACK already provides such a library for Angular, in @qlack/form-validation project, allowing you to seamlessly render your back-end validation errors to your Angular reactive form.

## Usage
This library comes as a `RestControllerAdvice` therefore no special configuration is required in your Spring Boot project; just add it in the list of your project's dependencies.

## Custom validation errors
This utility will work out of the box catching and converting validation errors produced when using the @Valid annotation in your code. However, oftentimes you need to perform custom validations (or business checks) for which an annotation might not be available for. For example, a `createUser` method that checks to see if the provided username already belongs to another user. This kind of checks require you to write custom code and raise a validation error programmatically.

The approach you find being suggested in such cases is to create a custom validator class. This is a class implementing the `ConstraintValidator` interface, providing the necessary validation logic for your specific use case. To be able to use this class in your code, you also need a matching annotation for it to indicate where to apply the underlying validator's logic. This is a fine approach when you have a handful of custom validators, or if your custom validators can be reused across different use cases. However, if you find yourself in need of implementing multiple custom validators throughout your project, it may become tedious having to maintain two separate files for each.

### QValidationUtil
QLACK Util validation provides a helper class, `QValidationUtil`, allowing you to programmatically raise validation errors without having to implement custom validators and annotations. To be able to use this helper class, you need to inject into your method  `org.springframework.validation.Errors` and `org.springframework.validation.BindingResult`.

Once you have the above variables injected, you can throw custom validation errors in any place in your code, for example:
```
new QValidationUtil(errors, bindingResult)
.throwValidationError("name", "TAG_ALREADY_EXISTS", "Tag name already exists.");
```
`QValidationUtil` also provides methods to stack multiple validation errors and only throw them when you need to.
