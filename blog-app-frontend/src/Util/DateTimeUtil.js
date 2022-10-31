export const PrintTimeStamp = (time) => {
    const date = new Date(time);
    return date.toLocaleString();
}

export const PrintDate = (time) => {
    const date = new Date(time);
    return date.toLocaleDateString();
}

export const PrintTime = (time) => {
    const date = new Date(time);
    return date.toLocaleTimeString();
}