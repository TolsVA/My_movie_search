package com.example.my_movie_search.model

open class AppException : RuntimeException()

class PasswordMismatchException : AppException()

class AccountAlreadyExistsException : AppException()

class AuthException : AppException()

class StorageException: AppException()