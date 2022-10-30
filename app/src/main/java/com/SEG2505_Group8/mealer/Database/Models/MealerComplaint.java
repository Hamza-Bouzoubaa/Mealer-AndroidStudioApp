package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerComplaint implements MealerSerializable {

    /**
     * Document id of Complaint
     */
    String id;

    /**
     * Id of chef who is the subject of the complaint
     */
    @MealerSerializableElement(key = "chefId")
    String chefId;

    /**
     * Id of the user who is making the complaint
     */
    @MealerSerializableElement(key = "userId")
    String userId;

    /**
     * Description of the complaint
     */
    @MealerSerializableElement(key = "description")
    String description;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
