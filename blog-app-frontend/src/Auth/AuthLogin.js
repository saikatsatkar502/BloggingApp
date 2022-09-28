// is login => check data from session storage

export const isLogin = () => {
    const data = sessionStorage.getItem("token");
    if (data != null)
        return true;
    else
        return false;
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
    const data = JSON.parse(sessionStorage.getItem("user" || null));
    if (data != null) {
        return data;
    } else
        return null;
}
