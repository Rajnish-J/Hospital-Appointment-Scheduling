import React, { Component } from "react";
import { Col, Container, Row, Button } from "react-bootstrap";
import { BrowserRouter as Router, Route, Link, Routes } from "react-router-dom";
import "../centre/centre.css";
import { UserContext } from "../../Login/login.jsx";

import FetchPatientDetails from "../../fetchPatientDetails/fetchPatientDetails.jsx";
import FetchAllAppointments from "../../fetchallAppointments/fetchallAppointments.jsx";
import UpdatePatientDetails from "../../updatePatientDetails/updatePatientDetails.jsx";
import AddAppointment from "../../AddAppointment/AddAppointment.jsx";
import UpdateAppointmentDetails from "../../UpdateAppointmentDetails/UpdateAppointmentDetails.jsx";
import DeleteAppointments from "../../DeleteAppointment/DeleteAppointment.jsx";
import DeletePatient from "../../DeletePatient/DeletePatient.jsx";

export default class centre extends Component {
  static contextType = UserContext;

  constructor(props) {
    super(props);
    this.state = {
      activeRoute: "",
    };
  }

  handleButtonClick = (route) => {
    this.setState({ activeRoute: route });
  };

  render() {
    const { activeRoute } = this.state;
    return (
      <div>
        {/* Router for the menu part and content part */}
        <Router>
          {/* Container tag */}
          <Container fluid>
            {/* Single row for the menu and content parts */}
            <Row className="ContentRow">
              {/* first column: menu */}
              <Col sm={2} xs={2} md={2} lg={2} className="menuCol">
                {/* div: menu */}
                <div className="menuDiv">
                  {/* fetch patient details by id */}
                  <Link to="/patientdetails">
                    <Button
                      variant={
                        activeRoute === "/patientdetails" ? "primary" : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() => this.handleButtonClick("/patientdetails")}
                    >
                      Your Details
                    </Button>
                  </Link>

                  {/* fetching all patient details */}
                  <Link to="/fetchAllAppointments">
                    <Button
                      variant={
                        activeRoute === "/fetchAllAppointments"
                          ? "primary"
                          : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() =>
                        this.handleButtonClick("/fetchAllAppointments")
                      }
                    >
                      Appointment Details
                    </Button>
                  </Link>

                  {/* updating patient details */}
                  <Link to="/UpdatePatientDetails">
                    <Button
                      variant={
                        activeRoute === "/UpdatePatientDetails"
                          ? "primary"
                          : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() =>
                        this.handleButtonClick("/UpdatePatientDetails")
                      }
                    >
                      Update Personal Details
                    </Button>
                  </Link>

                  {/* Adding the appointments for the patients */}
                  <Link to="/AddAppointments">
                    <Button
                      variant={
                        activeRoute === "/AddAppointments" ? "primary" : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() => this.handleButtonClick("/AddAppointments")}
                    >
                      Add Appointment
                    </Button>
                  </Link>

                  {/* updating appointment details */}
                  <Link to="/UpdateAppointmentDetails">
                    <Button
                      variant={
                        activeRoute === "/UpdateAppointmentDetails"
                          ? "primary"
                          : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() =>
                        this.handleButtonClick("/UpdateAppointmentDetails")
                      }
                    >
                      Update Appointment Details
                    </Button>
                  </Link>

                  {/* delete appointment */}
                  <Link to="/DeleteAppointment">
                    <Button
                      variant={
                        activeRoute === "/DeleteAppointment"
                          ? "primary"
                          : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() =>
                        this.handleButtonClick("/DeleteAppointment")
                      }
                    >
                      Cancel Appointment
                    </Button>
                  </Link>

                  {/* delete patient account */}
                  <Link to="/DeletePatient">
                    <Button
                      variant={
                        activeRoute === "/DeletePatient" ? "primary" : "light"
                      }
                      className="w-100 mb-2 Button"
                      onClick={() => this.handleButtonClick("/DeletePatient")}
                    >
                      Delete Account
                    </Button>
                  </Link>
                </div>
              </Col>

              {/* second column: content showing column */}
              <Col sm={10} xs={10} md={10} lg={10} className="contentCol">
                <div className="content-col-2-background"></div>
                {/* Routes to the respective pages or components or API's */}
                <Routes>
                  {/* <Route path="/" element={<Admin />} /> */}

                  <Route
                    path="/patientdetails"
                    element={<FetchPatientDetails />}
                  />

                  <Route
                    path="/fetchAllAppointments"
                    element={<FetchAllAppointments />}
                  />

                  <Route
                    path="/UpdatePatientDetails"
                    element={<UpdatePatientDetails />}
                  />

                  <Route path="/AddAppointments" element={<AddAppointment />} />

                  <Route
                    path="/UpdateAppointmentDetails"
                    element={<UpdateAppointmentDetails />}
                  />

                  <Route
                    path="/DeleteAppointment"
                    element={<DeleteAppointments />}
                  />

                  <Route path="/DeletePatient" element={<DeletePatient />} />
                </Routes>
              </Col>
            </Row>
          </Container>
        </Router>
      </div>
    );
  }
}
