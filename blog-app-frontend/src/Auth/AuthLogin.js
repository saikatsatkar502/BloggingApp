// is login => check data from session storage

export const isLogin = () => {
    const data = sessionStorage.getItem("token");
    return (data != null) ? true : false;
};

//doLogin => data => set to sessionStorage

export const doLogin = (data, next) => {
    sessionStorage.setItem("token", JSON.stringify(data.token));
    sessionStorage.setItem("user", JSON.stringify(data.user));
    next();
};


//doLogout => data => remove from sessionStorage

export const doLogout = (next) => {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("user");
    next();
};

//get current user => get data from session storage

export const getCurrentUser = () => {
    if (isLogin()) {
        return JSON.parse(sessionStorage.getItem("user" || null));
    } else {
        return null;
    }

}

//get current token => get data from session storage

export const getCurrentToken = () => {
    if (isLogin()) {
        return JSON.parse(sessionStorage.getItem("token" || null));
    } else {
        return null;
    }
}

