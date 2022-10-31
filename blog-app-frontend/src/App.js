import { Route, Routes } from "react-router-dom";
import About from "./Pages/About";
import ContactUs from "./Pages/ContactUs";
import Home from "./Pages/Home";
import Login from "./Pages/Login";
import Signup from "./Pages/Signup";
import 'react-toastify/dist/ReactToastify.css'
import "./App.css";
import Services from "./Pages/Services";
import { ToastContainer } from "react-toastify";
import Dashboard from "./Pages/User-Page/Dashboard";
import PrivateRoutes from "./Routes/PrivateRoutes";
import UserProfile from "./Pages/User-Page/UserProfile";
import UserPost from "./Pages/User-Page/UserPost";
import NewsFeeds from "./Pages/NewsFeeds";


function App() {
  return (
    <div className="App">
      <ToastContainer position='top-center' autoClose={3000} />
      <Routes>
        <Route exact path="/" element={<Home />} />
        <Route exact path="/news-feeds" element={<NewsFeeds />} />
        <Route exact path="/about" element={<About />} />
        <Route exact path="/signup" element={<Signup />} />
        <Route exact path="/login" element={<Login />} />
        <Route exact path="/contact" element={<ContactUs />} />
        <Route exact path="/services" element={<Services />} />

        <Route exact path="/user" element={<PrivateRoutes />}>
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="profile" element={<UserProfile />} />
          <Route path="posts" element={<UserPost />} />
        </Route>
        <Route path="*" element={<h1>404 Not Found</h1>} />
      </Routes>

    </div>
  );
}

export default App;
