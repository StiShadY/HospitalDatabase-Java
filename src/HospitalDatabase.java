//-----------------------------------------------------
// Title: Hospital Database Implementation
// Author: Ege Yavuz
// ID: 14872032366
// Section: 1
// Assignment: 3
// Using binary search trees, this class allows for the insertion, deletion,
// and querying of patients and their care teams in a hospital database.
//-----------------------------------------------------


import java.util.*;

import java.util.function.Consumer;

public class HospitalDatabase {

    // Inner class to represent a patient
    //--------------------------------------------------------
    // Summary: Represents a patient in the hospital database.
    // Contains details like name, doctor's name, and visit date.
    //--------------------------------------------------------
    private static class Patient implements Comparable<Patient> {
        private String name;
        private String doctorName;
        private int visitDay, visitMonth, visitYear;

        public Patient(String name, String doctorName, int visitDay, int visitMonth, int visitYear) {
            this.name = name;
            this.doctorName = doctorName;
            this.visitDay = visitDay;
            this.visitMonth = visitMonth;
            this.visitYear = visitYear;
        }

        public String getDoctorName() {
            return doctorName;
        }

        @Override
        public int compareTo(Patient other) {
            return this.name.compareTo(other.name); // Compare by name only
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Patient patient = (Patient) obj;
            return this.name.equals(patient.name); // Match patients by name only
        }

        @Override
        public String toString() {
            return name + ", " + visitYear + ", " + doctorName;
        }
    }

    private BinarySearchTree<Patient> patientTree;
    private HashMap<String, BinarySearchTree<String>> careTeams;

    // Constructor: Initializes the hospital database
    //--------------------------------------------------------
    // Summary: Creates an empty database with a binary search
    // tree for storing patients and a hash map for managing
    // care teams.
    // Precondition: None.
    // Postcondition: The database is initialized and ready for use.
    //--------------------------------------------------------

    public HospitalDatabase() {
        this.patientTree = new BinarySearchTree<>();
        this.careTeams = new HashMap<>();
    }

    // Method: Adds a new patient to the database
    //--------------------------------------------------------
    // Summary: Inserts a patient into the database. If a patient
    // with the same name already exists, their details are overwritten.
    // Precondition: The patient's name must be unique within the database.
    // Postcondition: The patient is added to the binary search tree, and
    // their care team is initialized.
    //--------------------------------------------------------

    public void addPatient(String patientName, String doctorName, int visitDay, int visitMonth, int visitYear) {
        Patient newPatient = new Patient(patientName, doctorName, visitDay, visitMonth, visitYear);

        if (patientTree.contains(newPatient)) {
            System.out.println("ERROR: Patient " + patientName + " overwritten");
        } else {
            System.out.println("INFO: Patient " + patientName + " has been added");
        }
        patientTree.insert(newPatient);
        careTeams.putIfAbsent(patientName, new BinarySearchTree<>());
    }

    // Method: Removes a patient from the database
    //--------------------------------------------------------
    // Summary: Deletes a patient and their associated care team
    // from the database.
    // Precondition: The patient's name must exist in the database.
    // Postcondition: The patient is removed from the binary search
    // tree and their care team is deleted.
    //--------------------------------------------------------

    public void removePatient(String patientName) {
        if (!careTeams.containsKey(patientName)) {
            System.out.println("ERROR: Patient " + patientName + " does not exist");
            return;
        }

        careTeams.remove(patientName);

        // Create a dummy patient with only the name for removal
        patientTree.remove(new Patient(patientName, "", 0, 0, 0));
        System.out.println("INFO: Patient " + patientName + " has been removed");
    }





    // Method: Adds a medical staff member to a patient's care team
    //--------------------------------------------------------
    // Summary: Adds a new staff member to the specified patient's
    // care team. If the staff member already exists in the care team,
    // their details are not duplicated.
    // Precondition: The patient must exist in the database.
    // Postcondition: The staff member is added to the patient's care team.
    //--------------------------------------------------------

    public void addMember(String patientName, String memberName, String memberRole) {
        if (!careTeams.containsKey(patientName)) {
            System.out.println("ERROR: Patient " + patientName + " does not exist");
            return;
        }

        BinarySearchTree<String> careTeam = careTeams.get(patientName);
        String staffInfo = memberName + ", " + memberRole;

        if (careTeam.contains(staffInfo)) {
            System.out.println("ERROR: Staff member " + memberName + " already exists in " + patientName + "'s care team");
        } else {
            System.out.println("INFO: " + memberName + " has been added to the patient " + patientName);
        }
        careTeam.insert(staffInfo);
    }

    // Method: Removes a medical staff member from a patient's care team
    //--------------------------------------------------------
    // Summary: Removes a staff member from the specified patient's
    // care team. If the staff member does not exist in the care team,
    // an error message is displayed.
    // Precondition: The patient must exist in the database, and the staff
    // member must exist in the patient's care team.
    // Postcondition: The staff member is removed from the care team.
    //--------------------------------------------------------


    public void removeMember(String patientName, String memberName) {
        if (!careTeams.containsKey(patientName)) {
            System.out.println("ERROR: Patient " + patientName + " does not exist");
            return;
        }

        BinarySearchTree<String> careTeam = careTeams.get(patientName);

        // Find the exact string for the staff member using a prefix match
        final String[] staffToRemove = {null};
        careTeam.inOrderTraversal(new Consumer<String>() {
            @Override
            public void accept(String member) {
                if (member.startsWith(memberName + ",")) {
                    staffToRemove[0] = member;
                }
            }
        });

        if (staffToRemove[0] != null) {
            careTeam.remove(staffToRemove[0]); // Remove the exact match
            System.out.println("INFO: " + memberName + " has been removed from the patient " + patientName);
        } else {
            System.out.println("ERROR: Staff member " + memberName + " does not exist in " + patientName + "'s care team");
        }
    }


    // Method: Displays all patients in the database
    //--------------------------------------------------------
    // Summary: Prints all patients stored in the binary search tree
    // in their natural order (by visit year and name).
    // Precondition: The database may be empty or contain patients.
    // Postcondition: The list of patients is displayed, or "---none---"
    // is shown if the database is empty.
    //--------------------------------------------------------

    public void showAllPatients() {
        if (careTeams.isEmpty()) {
            System.out.println("---none---");
            return;
        }

        // Step 1: Collect patients in a list
        List<Patient> patients = new ArrayList<>();
        patientTree.inOrderTraversal(patients::add);

        // Step 2: Sort the list by visitYear
        patients.sort(Comparator.comparingInt(patient -> patient.visitYear));

        // Step 3: Display the sorted patients
        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }


    // Method: Shows detailed information about a specific patient
    //--------------------------------------------------------
    // Summary: Displays all details of a specific patient, including
    // their visit date, doctor, and care team.
    // Precondition: The patient must exist in the database.
    // Postcondition: The patient's details are displayed, or "---none---"
    // if the patient does not exist.
    //--------------------------------------------------------

    public void showPatient(String patientName) {
        if (!careTeams.containsKey(patientName)) {
            System.out.println("---none---");
            return;
        }

        Patient patient = findPatient(patientName);
        System.out.println(patient.name);
        System.out.println(patient.visitDay + "/" + patient.visitMonth + "/" + patient.visitYear);
        System.out.println(patient.getDoctorName());

        BinarySearchTree<String> careTeam = careTeams.get(patientName);
        careTeam.inOrderTraversal(new Consumer<String>() {
            @Override
            public void accept(String member) {
                System.out.println(member);
            }
        });
    }

    // Helper Method: Finds a specific patient in the database
    //--------------------------------------------------------
    // Summary: Locates a patient by their name in the binary search tree.
    // Precondition: The binary search tree is initialized.
    // Postcondition: Returns the patient object if found, otherwise null.
    //--------------------------------------------------------

    private Patient findPatient(String patientName) {
        final Patient[] found = new Patient[1];
        patientTree.inOrderTraversal(new Consumer<Patient>() {
            @Override
            public void accept(Patient patient) {
                if (patient.name.equals(patientName)) {
                    found[0] = patient;
                }
            }
        });
        return found[0];
    }

    // Method: Queries patients seen by a specific doctor
    //--------------------------------------------------------
    // Summary: Displays all patients treated by the specified doctor.
    // Precondition: The database may contain patients treated by the doctor.
    // Postcondition: The list of patients is displayed.
    //--------------------------------------------------------

    public void showDoctorPatients(String doctorName) {
        System.out.println(doctorName);
        patientTree.inOrderTraversal(new Consumer<Patient>() {
            @Override
            public void accept(Patient patient) {
                if (patient.getDoctorName().equals(doctorName)) {
                    System.out.println(patient.name + ", " + patient.visitDay + "/" + patient.visitMonth + "/" + patient.visitYear);
                }
            }
        });
    }

    // Method: Queries patients by year
    //--------------------------------------------------------
    // Summary: Displays all patients who visited in the specified year,
    // ordered by their most recent visit (last entrance).
    // Precondition: The database may contain patients for the given year.
    // Postcondition: The list of patients for the year is displayed, or
    // an empty list if no patients visited in that year.
    //--------------------------------------------------------

    public void showPatients(int visitYear) {
        System.out.println(visitYear);

        // Step 1: Collect patients for the given year
        List<Patient> patients = new ArrayList<>();
        patientTree.inOrderTraversal(new Consumer<Patient>() {
            @Override
            public void accept(Patient patient) {
                if (patient.visitYear == visitYear) {
                    patients.add(patient); // Add patients to the list
                }
            }
        });

        // Step 2: Sort patients in descending order by visit date
        patients.sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient p1, Patient p2) {
                if (p1.visitMonth != p2.visitMonth) {
                    return Integer.compare(p2.visitMonth, p1.visitMonth); // Sort by month (descending)
                }
                return Integer.compare(p2.visitDay, p1.visitDay); // Sort by day (descending)
            }
        });

        // Step 3: Display the sorted patients
        for (Patient patient : patients) {
            System.out.println(patient.name + ", " + patient.visitDay + "/" + patient.visitMonth);
        }
    }







}
