//  ! after validation
import React, { Component } from "react";
import { Container, Table, Button, Form, Card } from "react-bootstrap";
import { UserContext } from "../Login/login.jsx"; // Import UserContext for accessing context data

export default class AppointmentList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedAppointment: null, // Stores the appointment being updated
      appointmentDate: "",
      reason: "",
      errorMessage: "",
      successMessage: "",
    };
  }

  static contextType = UserContext;

  // Handles input changes in the form
  handleChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  validateInputs = () => {
    const { appointmentDate, reason } = this.state;

    // Validate appointment date is not in the past
    if (appointmentDate) {
      const selectedDate = new Date(appointmentDate);
      const currentDate = new Date();
      currentDate.setHours(0, 0, 0, 0); // Normalize time for comparison
      if (selectedDate < currentDate) {
        this.setState({
          errorMessage: "Appointment date cannot be in the past.",
        });
        return false;
      }
    }

    // Validate reason length
    if (reason.length > 50) {
      this.setState({
        errorMessage: "Reason must be less than 50 characters.",
      });
      return false;
    }
    // Reset error message if validation passes
    this.setState({ errorMessage: "" });
    return true;
  };

  // Handles the Update button click and sets the selected appointment
  handleUpdateClick = (appointment) => {
    this.setState({
      selectedAppointment: appointment,
      appointmentDate: appointment.appointmentDate,
      reason: appointment.reason,
      errorMessage: "",
      successMessage: "",
    });
  };

  // Handles the form submission to update the appointment\

  handleSubmit = (e) => {
    e.preventDefault();

    if (!this.validateInputs()) {
      return;
    }

    const { selectedAppointment, appointmentDate, reason } = this.state;

    // First, check if the selected new appointment date has less than 5 appointments
    const apiUrl = `http://localhost:8080/appointment/countOfAppointmentsByDate/${appointmentDate}`;
    fetch(apiUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch appointment count.");
        }
        return response.json();
      })
      .then((count) => {
        if (count >= 5) {
          // If the count exceeds or is equal to 5, show error message
          this.setState({
            errorMessage:
              "All appointments are booked for this day already. Please select another convenient day for consulting doctors.",
            successMessage: "",
          });
        } else {
          // If the count is less than 5, proceed with updating the appointment
          const updatedAppointment = { appointmentDate, reason };

          fetch(
            `http://localhost:8080/appointment/updateAppointments/${selectedAppointment.appointmentID}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(updatedAppointment),
            }
          )
            .then((response) => {
              if (!response.ok) {
                throw new Error("Failed to update appointment");
              }
              return response.text(); // Expect plain text response
            })
            .then((message) => {
              this.setState({
                successMessage: message, // Display the plain text success message
                errorMessage: "",
              });

              // Update context with new appointment data
              const updatedAppointments = this.context.appointments.map(
                (appointment) =>
                  appointment.appointmentID ===
                  selectedAppointment.appointmentID
                    ? { ...appointment, appointmentDate, reason }
                    : appointment
              );
              this.context.setAppointments(updatedAppointments); // Update context

              // Reset selected appointment after success
              this.setState({ selectedAppointment: null });
            })
            .catch((error) => {
              this.setState({
                errorMessage: error.message || "An error occurred",
                successMessage: "",
              });
            });
        }
      })
      .catch(() => {
        this.setState({
          errorMessage: "Error checking appointment availability.",
          successMessage: "",
        });
      });
  };

  render() {
    const { appointments } = this.context; // Fetch appointments from context
    const {
      selectedAppointment,
      appointmentDate,
      reason,
      errorMessage,
      successMessage,
    } = this.state;

    return (
      <Container className="mt-5">
        <h3 className="text-center mb-4">Appointments</h3>

        {/* Display success or error messages */}
        {errorMessage && (
          <div className="text-danger text-center mb-3">{errorMessage}</div>
        )}
        {successMessage && (
          <div className="text-success text-center mb-3">{successMessage}</div>
        )}

        {/* Conditional rendering for appointments */}
        {appointments.length === 0 ? (
          <Card className="mt-4 text-center">
            <Card.Body>
              <Card.Title>No Appointments Found</Card.Title>
            </Card.Body>
          </Card>
        ) : (
          <>
            {/* Table for displaying appointments */}
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Date</th>
                  <th>Reason</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {appointments.map((appointment) => {
                  const appointmentDate = new Date(appointment.appointmentDate);
                  const currentDate = new Date();
                  currentDate.setHours(0, 0, 0, 0); // Normalize time for comparison
                  // Only show Update button if the appointment is today or in the future
                  const showUpdateButton = appointmentDate >= currentDate;

                  return (
                    <tr key={appointment.appointmentID}>
                      <td>{appointment.appointmentID}</td>
                      <td>{appointment.appointmentDate}</td>
                      <td>{appointment.reason}</td>
                      <td>
                        {showUpdateButton && (
                          <Button
                            variant="primary"
                            onClick={() => this.handleUpdateClick(appointment)}
                          >
                            Update
                          </Button>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </Table>

            {/* Form to update the selected appointment */}
            {selectedAppointment && (
              <Card className="mt-4">
                <Card.Body>
                  <h5 className="text-center">Update Appointment</h5>
                  <Form onSubmit={this.handleSubmit}>
                    <Form.Group controlId="appointmentDate" className="mb-3">
                      <Form.Label>Appointment Date</Form.Label>
                      <Form.Control
                        type="date"
                        name="appointmentDate"
                        value={appointmentDate}
                        onChange={this.handleChange}
                        isInvalid={
                          errorMessage ===
                          "Appointment date cannot be in the past."
                        }
                        required
                      />
                      <Form.Control.Feedback type="invalid">
                        Appointment date cannot be in the past.
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group controlId="reason" className="mb-3">
                      <Form.Label>Reason</Form.Label>
                      <Form.Control
                        type="text"
                        name="reason"
                        value={reason}
                        onChange={this.handleChange}
                        isInvalid={
                          errorMessage ===
                          "Reason must be less than 50 characters."
                        }
                        required
                        placeholder="Enter reason"
                      />
                      <Form.Control.Feedback type="invalid">
                        Reason must be less than 50 characters.
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Button variant="success" type="submit" className="w-100">
                      Save Changes
                    </Button>
                  </Form>
                </Card.Body>
              </Card>
            )}
          </>
        )}
      </Container>
    );
  }
}
