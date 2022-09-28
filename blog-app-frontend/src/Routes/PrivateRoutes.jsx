import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'
import { isLogin } from '../Auth/AuthLogin'

function PrivateRoutes() {
    return (
        isLogin() ? <Outlet /> : <Navigate to="/login" />
    )

}

export default PrivateRoutes;