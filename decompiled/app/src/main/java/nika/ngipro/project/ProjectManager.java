package nika.ngipro.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Project Workspace Manager - Save and load entire analysis sessions
 * Supports annotations, findings, and project metadata
 */
public class ProjectManager {
    
    public static class Project implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String projectId;
        public String projectName;
        public String apkPath;
        public String apkName;
        public long apkSize;
        public String apkHash;
        public long createdDate;
        public long lastModifiedDate;
        public String ngiProVersion;
        
        // Analysis Results
        public Map<String, Object> cryptoAnalysisResults = new HashMap<>();
        public Map<String, Object> trackerAnalysisResults = new HashMap<>();
        public Map<String, Object> stringDecryptionResults = new HashMap<>();
        public Map<String, Object> secretScanResults = new HashMap<>();
        
        // Annotations and Notes
        public List<Annotation> annotations = new ArrayList<>();
        
        // Custom Class/Method Names (for deobfuscation)
        public Map<String, String> renamedClasses = new HashMap<>();
        public Map<String, String> renamedMethods = new HashMap<>();
        
        // Bookmarks/Favorites
        public List<Bookmark> bookmarks = new ArrayList<>();
        
        // Analysis Settings
        public ProjectSettings settings = new ProjectSettings();
        
        // Metadata
        public Map<String, String> metadata = new HashMap<>();
        
        public Project() {
            this.projectId = UUID.randomUUID().toString();
            this.createdDate = System.currentTimeMillis();
            this.lastModifiedDate = System.currentTimeMillis();
            this.ngiProVersion = "2.0";
        }
        
        public Project(String name, String apkPath) {
            this();
            this.projectName = name;
            this.apkPath = apkPath;
            
            File apkFile = new File(apkPath);
            if (apkFile.exists()) {
                this.apkName = apkFile.getName();
                this.apkSize = apkFile.length();
            }
        }
        
        public void touch() {
            this.lastModifiedDate = System.currentTimeMillis();
        }
        
        public void addAnnotation(Annotation annotation) {
            annotations.add(annotation);
            touch();
        }
        
        public void addBookmark(Bookmark bookmark) {
            bookmarks.add(bookmark);
            touch();
        }
        
        public void renameClass(String originalName, String newName) {
            renamedClasses.put(originalName, newName);
            touch();
        }
        
        public void renameMethod(String originalName, String newName) {
            renamedMethods.put(originalName, newName);
            touch();
        }
        
        @Override
        public String toString() {
            return String.format("Project: %s\nAPK: %s\nCreated: %tF\nAnnotations: %d\nBookmarks: %d",
                projectName, apkName, createdDate, annotations.size(), bookmarks.size());
        }
    }
    
    public static class Annotation implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String id;
        public String targetType; // CLASS, METHOD, FIELD, STRING, etc.
        public String targetIdentifier; // Fully qualified name or location
        public String content;
        public String category; // NOTE, WARNING, TODO, IMPORTANT, etc.
        public int priority; // 1-5
        public long createdDate;
        public String color; // Hex color code
        
        public Annotation() {
            this.id = UUID.randomUUID().toString();
            this.createdDate = System.currentTimeMillis();
            this.priority = 3;
            this.category = "NOTE";
            this.color = "#FFEB3B";
        }
        
        public Annotation(String targetType, String targetIdentifier, String content) {
            this();
            this.targetType = targetType;
            this.targetIdentifier = targetIdentifier;
            this.content = content;
        }
        
        public static Annotation createNote(String location, String note) {
            return new Annotation("LOCATION", location, note);
        }
        
        public static Annotation createWarning(String className, String warning) {
            Annotation ann = new Annotation("CLASS", className, warning);
            ann.category = "WARNING";
            ann.color = "#FF5722";
            ann.priority = 4;
            return ann;
        }
        
        public static Annotation createTodo(String method, String todo) {
            Annotation ann = new Annotation("METHOD", method, todo);
            ann.category = "TODO";
            ann.color = "#2196F3";
            ann.priority = 2;
            return ann;
        }
    }
    
    public static class Bookmark implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String id;
        public String title;
        public String targetType; // CLASS, METHOD, FILE
        public String targetIdentifier;
        public String description;
        public long createdDate;
        
        public Bookmark() {
            this.id = UUID.randomUUID().toString();
            this.createdDate = System.currentTimeMillis();
        }
        
        public Bookmark(String title, String targetType, String identifier) {
            this();
            this.title = title;
            this.targetType = targetType;
            this.targetIdentifier = identifier;
        }
    }
    
    public static class ProjectSettings implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public boolean enableCryptoAnalysis = true;
        public boolean enableStringDecryption = true;
        public boolean enableTrackerDetection = true;
        public boolean enableSecretScanning = true;
        public boolean autoSave = true;
        public int autoSaveIntervalMinutes = 5;
        public String exportFormat = "JSON"; // JSON, XML, CSV
        public boolean includeSourceCode = false;
        public int maxStringLength = 1000;
    }
    
    private static final String PROJECTS_DIR = "ngipro_projects";
    private static final String PROJECT_EXTENSION = ".ngiproj";
    
    private File projectsDirectory;
    
    public ProjectManager() {
        this.projectsDirectory = new File(System.getProperty("user.home"), PROJECTS_DIR);
        if (!projectsDirectory.exists()) {
            projectsDirectory.mkdirs();
        }
    }
    
    public ProjectManager(String customProjectsDir) {
        this.projectsDirectory = new File(customProjectsDir);
        if (!projectsDirectory.exists()) {
            projectsDirectory.mkdirs();
        }
    }
    
    /**
     * Create a new project
     */
    public Project createProject(String projectName, String apkPath) {
        Project project = new Project(projectName, apkPath);
        return project;
    }
    
    /**
     * Save project to disk
     */
    public boolean saveProject(Project project) {
        try {
            project.touch();
            File projectFile = new File(projectsDirectory, 
                sanitizeFileName(project.projectName) + PROJECT_EXTENSION);
            
            try (FileOutputStream fos = new FileOutputStream(projectFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(project);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load project from disk
     */
    public Project loadProject(String projectName) {
        try {
            File projectFile = new File(projectsDirectory, 
                sanitizeFileName(projectName) + PROJECT_EXTENSION);
            
            if (!projectFile.exists()) {
                throw new IOException("Project not found: " + projectName);
            }
            
            try (FileInputStream fis = new FileInputStream(projectFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (Project) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Load project by file path
     */
    public Project loadProjectFromFile(File projectFile) {
        try {
            if (!projectFile.exists()) {
                throw new IOException("Project file not found");
            }
            
            try (FileInputStream fis = new FileInputStream(projectFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (Project) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Delete project
     */
    public boolean deleteProject(String projectName) {
        File projectFile = new File(projectsDirectory, 
            sanitizeFileName(projectName) + PROJECT_EXTENSION);
        return projectFile.delete();
    }
    
    /**
     * List all available projects
     */
    public List<Project> listProjects() {
        List<Project> projects = new ArrayList<>();
        File[] projectFiles = projectsDirectory.listFiles((dir, name) -> 
            name.endsWith(PROJECT_EXTENSION));
        
        if (projectFiles != null) {
            for (File projectFile : projectFiles) {
                Project project = loadProjectFromFile(projectFile);
                if (project != null) {
                    projects.add(project);
                }
            }
        }
        
        return projects;
    }
    
    /**
     * Search projects by name
     */
    public List<Project> searchProjects(String query) {
        List<Project> allProjects = listProjects();
        List<Project> results = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        for (Project project : allProjects) {
            if (project.projectName.toLowerCase().contains(lowerQuery) ||
                (project.apkName != null && project.apkName.toLowerCase().contains(lowerQuery))) {
                results.add(project);
            }
        }
        
        return results;
    }
    
    /**
     * Export project to JSON (simplified version)
     */
    public String exportProjectToJson(Project project) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"projectId\": \"").append(project.projectId).append("\",\n");
        json.append("  \"projectName\": \"").append(escapeJson(project.projectName)).append("\",\n");
        json.append("  \"apkName\": \"").append(escapeJson(project.apkName)).append("\",\n");
        json.append("  \"createdDate\": ").append(project.createdDate).append(",\n");
        json.append("  \"lastModifiedDate\": ").append(project.lastModifiedDate).append(",\n");
        json.append("  \"annotationsCount\": ").append(project.annotations.size()).append(",\n");
        json.append("  \"bookmarksCount\": ").append(project.bookmarks.size()).append(",\n");
        json.append("  \"renamedClassesCount\": ").append(project.renamedClasses.size()).append(",\n");
        json.append("  \"renamedMethodsCount\": ").append(project.renamedMethods.size()).append("\n");
        json.append("}\n");
        
        return json.toString();
    }
    
    /**
     * Get project statistics
     */
    public Map<String, Object> getProjectStatistics(Project project) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAnnotations", project.annotations.size());
        stats.put("totalBookmarks", project.bookmarks.size());
        stats.put("totalRenamedClasses", project.renamedClasses.size());
        stats.put("totalRenamedMethods", project.renamedMethods.size());
        stats.put("hasCryptoAnalysis", !project.cryptoAnalysisResults.isEmpty());
        stats.put("hasTrackerAnalysis", !project.trackerAnalysisResults.isEmpty());
        stats.put("hasSecretScan", !project.secretScanResults.isEmpty());
        
        // Count annotations by category
        Map<String, Integer> annotationsByCategory = new HashMap<>();
        for (Annotation ann : project.annotations) {
            annotationsByCategory.merge(ann.category, 1, Integer::sum);
        }
        stats.put("annotationsByCategory", annotationsByCategory);
        
        return stats;
    }
    
    /**
     * Auto-save project (background task)
     */
    public void autoSave(Project project) {
        if (project.settings.autoSave) {
            saveProject(project);
        }
    }
    
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
