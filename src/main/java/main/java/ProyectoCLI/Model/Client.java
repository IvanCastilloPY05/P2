package ProyectoCLI.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a Client in the system.
 * Each client has a name, a national identification number (numCI), an email,
 * and a registration date.
 *
 * @author Ivan Castillo
 */
public class Client {
    private String name;
    private String numCI; // National Identification Number
    private String email;
    private final LocalDate registrationDate;

    private static final String EMAIL_REGEX = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String NUMCI_REGEX = "^[0-9]+$";
    private static final Pattern NUMCI_PATTERN = Pattern.compile(NUMCI_REGEX);

    /**
     * Default constructor. Initializes fields to default empty/null values
     * and registration date to the current date.
     * This constructor might be less useful if all fields are considered mandatory.
     * Consider removing if a client must always be created with full details.
     */
    public Client() {
        this.name = "";
        this.numCI = "";
        this.email = "";
        this.registrationDate = LocalDate.now();
    }

    /**
     * Constructs a new Client with specified details.
     * The registration date is set to the current date upon creation.
     *
     * @param name The name of the client. Must not be null or empty.
     * @param numCI The national identification number of the client. Must not be null or empty and must be numeric.
     * @param email The email address of the client. Must not be null or empty and must be a valid format.
     * @throws IllegalArgumentException if any of the validation rules for parameters are violated.
     */
    public Client(String name, String numCI, String email) {
        setName(name);
        setNumCI(numCI);
        setEmail(email);
        this.registrationDate = LocalDate.now();
    }

    // Getters

    /**
     * Gets the client's name.
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the client's email address.
     * @return The email address of the client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the client's national identification number (CI).
     * @return The national identification number of the client.
     */
    public String getNumCI() {
        return numCI;
    }

    /**
     * Gets the date when the client was registered.
     * @return The registration date of the client.
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    // Setters

    /**
     * Sets the client's name.
     *
     * @param name The new name for the client. Must not be null or empty.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public void setName(String name) {
        validateNotEmpty(name, "Nombre del cliente");
        this.name = name.trim();
    }

    /**
     * Sets the client's email address.
     *
     * @param email The new email address for the client. Must be a valid email format and not null or empty.
     * @throws IllegalArgumentException if the email is null, empty, or invalid.
     */
    public void setEmail(String email) {
        validateNotEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Formato de email invalido: " + email);
        }
        this.email = email.trim();
    }

    /**
     * Sets the client's national identification number (CI).
     *
     * @param numCI The new national identification number for the client. Must be numeric and not null or empty.
     * @throws IllegalArgumentException if the numCI is null, empty, or not numeric.
     */
    public void setNumCI(String numCI) {
        validateNotEmpty(numCI, "Numero de cedula de identidad (numCI)");
        if (!NUMCI_PATTERN.matcher(numCI.trim()).matches()) {
            throw new IllegalArgumentException("Numero de cedula de identidad invalido (numCI): " + numCI + ". Debe ser numerico sin puntos.");
        }
        this.numCI = numCI.trim();
    }

    // Validation

    /**
     * Validates if a given string is null or empty after trimming.
     *
     * @param text The string to validate.
     * @param fieldName The name of the field being validated (for clear error messages).
     * @throws IllegalArgumentException if the text is null or empty.
     */
    private void validateNotEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacio o nulo.");
        }
    }

    // Utility Methods

    /**
     * Returns a string representation of the client's data.
     * Formats the registration date as "yyyy-MM-dd".
     *
     * @return A string containing the client's CI, name, email, and registration date.
     */
    public String getClientData() {
        return String.format("CI: %s, Nombre: %s, Email: %s, Fecha de registro: %s",
                numCI, name, email, registrationDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    /**
     * Returns a string representation of the Client object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return getClientData();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Equality is based on the numCI.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(numCI, client.numCI);
    }

    /**
     * Returns a hash code value for the object.
     * This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
     * The hash code is based on the numCI.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numCI);
    }
}
