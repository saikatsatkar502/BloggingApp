import { myAxios } from "../Api/Api"

export const signup = async (user) => {
    const response = await myAxios.post("/auth/register", user)
    return response.data
}

export const login = async (user) => {
    const response = await myAxios.post("/auth/login", user)
    return response.data
}

