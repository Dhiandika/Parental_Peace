from flask import Flask, request, jsonify
from waitress import serve
from predict import predict_class
import json