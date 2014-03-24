(ns differ-installer.core
  (:use [seesaw.core :only [frame form-panel label
                            text grid-panel button
                            pack! show! alert value!
                            select config! checkbox
                            ]]
        [seesaw.chooser :only [choose-file]]
        )
  (:gen-class :main true)
  )

(defn -main [& args]
  (let [frame (frame :title "Differ Installer")]
    (-> frame
        (config!  :content
                  (form-panel
                   :items [ [nil :fill :both :insets (java.awt.Insets. 5 5 5 5) :gridx 0 :gridy 0]
                            [(label :text "Directory to install:" :halign :right)]
                            [(text :columns 20 :id :install-directory) :grid :next :weightx 1.0]
                            [(button :text "Choose a dir" :id :choose-dir
                                     :listen [:action (fn [e] (value! (select frame [:#install-directory ])
                                                                     (.toString (choose-file :selection-mode :dirs-only))))]
                                     ) :grid :next]
                            [(grid-panel :columns 1
                                         :items [ (button :text "Install" :id :btn-install)
                                                  (button :text "Cancel" :id :btn-cancel)
                                                  ]
                                         ) :grid :next :gridheight 5 :anchor :north :weightx 0]
                            [(label :text "Choose extractors to install" :halign :right) :gridheight 1 :grid :wrap]
                            [[5 :by 5] :grid :wrap]
                            [(grid-panel :columns 2
                                         :items (map #(checkbox :text %) 
                                                     ["Fits" "Jhove" 
                                                      "Imagemagic" "jpylyzer"
                                                      "Daits" "Kakadu - free version"
                                                      "DAITSS" "DjvuDump"
                                                      "ExifTool" "Exiv2"
                                                      ]))
                             :grid :next]
                            ]
                   )
                  )
           pack!
           show!
           )
    )
  )
