    package jpabook.model.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.OneToMany;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    public class Team {

        static int tempId = 0;

        @Id
        @Column(name = "TEAM_ID")
        private String id;

        private String name;

        @OneToMany(mappedBy = "team")
        private List<Member> members = new ArrayList<Member>();

        public Team() {
            this.id = String.valueOf(tempId);
            tempId++;
        }

        public Team(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Member> getMembers() {
            return members;
        }
    }
