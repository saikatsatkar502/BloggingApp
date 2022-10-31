import axios from "axios";
import { getCurrentToken } from "../Auth/AuthLogin";

export const BASE_URL = "http://localhost:8082/";

export const myAxios = axios.create({
    baseURL: BASE_URL,
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Credentials": true,
        "mediaType": "json",
        "Accept": "application/json",
    }
})

export const privateMyAxios = axios.create({
    baseURL: BASE_URL,
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Credentials": true,
        "mediaType": "json",
        "Accept": "application/json",
    }
});

privateMyAxios.interceptors.request.use(config => {
    const token = getCurrentToken()
    if (token) {
        config.headers.common.Authorization = `Bearer ${token}`
        return config
    }
}, error => { Promise.reject(error) })

